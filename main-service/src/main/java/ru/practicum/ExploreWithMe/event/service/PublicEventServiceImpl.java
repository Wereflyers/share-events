package ru.practicum.ExploreWithMe.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.dto.Stat;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.event.EventMapper;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.LocationRepository;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.LocationDto;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;
import ru.practicum.ExploreWithMe.request.RequestRepository;
import ru.practicum.ExploreWithMe.statistics.StatisticService;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatisticService statisticService;

    /**
     * Получение событий по фильтрам
     * @param text по аннотации и подробному описанию, без учета регистра
     * @param categories по категориям
     * @param paid только платных/бесплатных
     * @param rangeStart если не указано, то сейчас
     * @param rangeEnd не позже
     * @param onlyAvailable только события, у которых не исчерпан лимит на участие default false
     * @param sort EVENT_DATE, VIEWS
     * @param from страница default 0
     * @param size страница default 10
     * @return List
     */
    @Override
    public List<EventFullDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (sort == null) {
            return eventRepository.searchWithoutSort(text.toUpperCase(), categories, paid, rangeStart, rangeEnd, page).stream()
                    .map(this::createResponse)
                    .collect(Collectors.toList());
        } else if (sort.equals("VIEWS")) {
            return eventRepository.searchWithoutSort(text.toUpperCase(), categories, paid, rangeStart, rangeEnd, page).stream()
                    .map(this::createResponse)
                    .sorted((e1, e2) -> e2.getViews() - e1.getViews())
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else if (sort.equals("EVENT_DATE")) {
            return eventRepository.searchWithSortByDate(text.toUpperCase(), categories, paid, rangeStart, rangeEnd, page).stream()
                    .map(this::createResponse)
                    .collect(Collectors.toList());
        } else throw  new WrongConditionException("Sort is not proper");
    }

    @Override
    public EventFullDto get(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("Event with id=" + eventId + " was not found."));
        if (event.getState() != State.PUBLISHED) {
            throw new NullPointerException("Event with id=" + eventId + " was not found.");
        }
        return createResponse(event);
    }

    private EventFullDto createResponse(Event event) {
        LocationDto locationDto = EventMapper.toLocationDto(locationRepository.findById(event.getLocation()).orElseThrow());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NullPointerException("Category with id=" + event.getCategory() + " was not found.")));
        User user = userRepository.findById(event.getInitiator()).orElseThrow(() -> new NullPointerException("User with id=" + event.getInitiator() + "is not found."));
        UserShortDto userShortDto = new UserShortDto(user.getId(), user.getName());
        int confirmedRequests = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, event.getId()).size();
        List<String> uris = List.of("/events/", "/events/" + event.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        Stat stat = objectMapper.convertValue(statisticService.getViews(uris).getBody(), Stat.class);
        return EventMapper.toEventFullDto(locationDto, categoryDto, userShortDto, event, confirmedRequests, stat.getHits());
    }
}
