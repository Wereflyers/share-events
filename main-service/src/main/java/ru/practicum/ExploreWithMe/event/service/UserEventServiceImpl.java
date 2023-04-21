package ru.practicum.ExploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.enums.StateAction;
import ru.practicum.ExploreWithMe.event.EventMapper;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.LocationRepository;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.LocationDto;
import ru.practicum.ExploreWithMe.event.dto.NewEventDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventUserRequest;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.event.model.Location;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        if (!eventDto.getEventDate().isAfter(LocalDateTime.now())) {
            throw new WrongConditionException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                    eventDto.getEventDate().toString());
        }
        Long locationId = saveLocation(eventDto.getLocation()).getId();
        Event event = eventRepository.save(EventMapper.toNewEvent(userId, locationId, eventDto));
        return createResponse(event);
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorAndId(userId, eventId);
        if (event == null) {
            throw new NullPointerException("Event with id=" + eventId + " was not found.");
        }
        return createResponse(event);
    }

    @Override
    public List<EventFullDto> getAllForUser(Long userId, int from, int size) {
        return eventRepository.findAllByInitiator(userId, PageRequest.of(from / size, size)).stream()
                .map(this::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = eventRepository.findByInitiatorAndId(userId, eventId);
        if (event == null) {
            throw new NullPointerException("Event with id=" + eventId + " was not found.");
        }
        if (event.getState().name().equals("PUBLISHED")) {
            throw new WrongConditionException("Only pending or canceled events can be changed");
        }
        if (updateEvent.getEventDate() != null && Duration.between(updateEvent.getEventDate(), LocalDateTime.now()).toHours() < 2) {
            throw new WrongConditionException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                    updateEvent.getEventDate().toString());
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(updateEvent.getCategory());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(saveLocation(updateEvent.getLocation()).getId());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getStateAction() == StateAction.CANCEL_REVIEW) event.setState(State.CANCELED);
        else event.setState(State.PENDING);
        return createResponse(eventRepository.save(event));
    }

    @Transactional
    public Location saveLocation(LocationDto locationDto) {
        Location location = locationRepository.findByLonAndLat(locationDto.getLon(), locationDto.getLat());
        if (location != null) {
            return location;
        }
        return locationRepository.save(EventMapper.toLocation(locationDto));
    }

    private EventFullDto createResponse(Event event) {
        LocationDto locationDto = EventMapper.toLocationDto(locationRepository.findById(event.getLocation()).orElseThrow());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NullPointerException("Category with id=" + event.getCategory() + " was not found.")));
        User user = userRepository.findById(event.getInitiator()).orElseThrow(() -> new NullPointerException("User with id=" + event.getInitiator() + "is not found."));
        UserShortDto userShortDto = new UserShortDto(user.getId(), user.getName());
        return EventMapper.toEventFullDto(locationDto, categoryDto, userShortDto, event);
    }
}
