package ru.practicum.ExploreWithMe.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.NewCompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;
import ru.practicum.ExploreWithMe.event.model.EventShort;
import ru.practicum.ExploreWithMe.exception.DuplicateException;
import ru.practicum.ExploreWithMe.request.RequestRepository;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsCompilationRepository eventsCompilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventsCompilationRepository eventsCompilationRepository, EventRepository eventRepository, RequestRepository requestRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.compilationRepository = compilationRepository;
        this.eventsCompilationRepository = eventsCompilationRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        try {
            Compilation compilation = compilationRepository.save(new Compilation(null, newCompilationDto.getTitle(), newCompilationDto.getPinned()));
            for (Long e : newCompilationDto.getEvents()) {
                eventsCompilationRepository.save(new CompilationEvents(null, e, compilation.getId()));
            }
            return createResponse(compilation);
        } catch (Exception e) {
            throw new DuplicateException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found"));
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found"));
        if (updateCompilationRequest.getTitle() != null) compilation.setTitle(updateCompilationRequest.getTitle());
        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());
        // TODO if (updateCompilationRequest.getEvents() != null)
        return createResponse(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto get(Long compId) {
        return createResponse(compilationRepository.findById(compId).orElseThrow(() -> new NullPointerException("Compilation with id=" + compId + " was not found")));
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned == null) {
            return compilationRepository.findAll(page).stream()
                    .map(this::createResponse)
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAllByPinned(pinned, page).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    private CompilationDto createResponse(Compilation compilation) {
        List<Long> events = eventsCompilationRepository.findEventsId(compilation.getId());
        List<EventShortDto> eventsShort = eventRepository.findEventsShort(events).stream()
                .map(this::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .events(eventsShort)
                .build();
    }

    private EventShortDto toEventShortDto(EventShort eventShort) {
        Integer confirmedReq = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, eventShort.getId()).size();
        CategoryDto category = CategoryMapper.toCategoryDto(categoryRepository.findById(eventShort.getCategory()).orElseThrow());
        User user = userRepository.findById(eventShort.getInitiator()).orElseThrow();
        //TODO views
        return EventShortDto.builder()
                .annotation(eventShort.getAnnotation())
                .category(category)
                .confirmedRequests(confirmedReq)
                .eventDate(eventShort.getEventDate())
                .id(eventShort.getId())
                .initiator(new UserShortDto(user.getId(), user.getName()))
                .paid(eventShort.getPaid())
                .title(eventShort.getTitle())
                .views(null)
                .build();
    }
}
