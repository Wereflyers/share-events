package ru.practicum.ExploreWithMe.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.category.CategoryMapper;
import ru.practicum.ExploreWithMe.category.CategoryRepository;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.enums.RequestStatus;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.enums.StateAction;
import ru.practicum.ExploreWithMe.event.EventMapper;
import ru.practicum.ExploreWithMe.event.EventRepository;
import ru.practicum.ExploreWithMe.event.LocationRepository;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.LocationDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventAdminRequest;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.event.model.Location;
import ru.practicum.ExploreWithMe.exception.WrongConditionException;
import ru.practicum.ExploreWithMe.request.RequestRepository;
import ru.practicum.ExploreWithMe.statistics.StatService;
import ru.practicum.ExploreWithMe.user.User;
import ru.practicum.ExploreWithMe.user.UserRepository;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final StatService statService;

    private EventFullDto createResponse(Event event) {
        LocationDto locationDto = EventMapper.toLocationDto(locationRepository.findById(event.getLocation()).orElseThrow());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new NullPointerException("Category with id=" + event.getCategory() + " was not found.")));
        User user = userRepository.findById(event.getInitiator()).orElseThrow(() -> new NullPointerException("User with id=" + event.getInitiator() + "is not found."));
        UserShortDto userShortDto = new UserShortDto(user.getId(), user.getName());
        int confirmedRequests = requestRepository.findAllByStatusAndEvent(RequestStatus.CONFIRMED, event.getId()).size();
        List<String> uris = List.of("/events/", "/events/" + event.getId());
        return EventMapper.toEventFullDto(locationDto, categoryDto, userShortDto, event, confirmedRequests, statService.count(uris).getViews());
    }

    @Transactional
    public Location saveLocation(LocationDto locationDto) {
        Location location = locationRepository.findByLonAndLat(locationDto.getLon(), locationDto.getLat());
        if (location != null) {
            return location;
        }
        return locationRepository.save(EventMapper.toLocation(locationDto));
    }

    @Override
    public List<EventFullDto> getAll(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        List<State> st = null;
        if (states != null){
            st = states.stream()
                    .map(State::valueOf)
                    .collect(Collectors.toList());
        }
        return eventRepository.getAllWithDateFilter(users, st, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size)).stream()
                    .map(this::createResponse)
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("Event with id=" + eventId + " was not found."));
        if ((eventDto.getStateAction() == StateAction.PUBLISH_EVENT && event.getState() != State.PENDING) ||
                (eventDto.getStateAction() == StateAction.REJECT_EVENT && event.getState() == State.PUBLISHED)) {
            throw new WrongConditionException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (eventDto.getEventDate() != null) {
            if ((event.getPublishedOn() != null && Duration.between(event.getPublishedOn(), eventDto.getEventDate()).toHours() < 1) ||
                    Objects.equals(eventDto.getEventDate(), LocalDateTime.now()) || eventDto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new WrongConditionException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                        eventDto.getEventDate().toString());
            }
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getAnnotation() != null) event.setAnnotation(eventDto.getAnnotation());
        if (eventDto.getCategory() != null) event.setCategory(eventDto.getCategory());
        if (eventDto.getDescription() != null) event.setDescription(eventDto.getDescription());
        if (eventDto.getLocation() != null) event.setLocation(saveLocation(eventDto.getLocation()).getId());
        if (eventDto.getPaid() != null) event.setPaid(eventDto.getPaid());
        if (eventDto.getParticipantLimit() != null) event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getRequestModeration() != null) event.setRequestModeration(eventDto.getRequestModeration());
        if (eventDto.getTitle() != null) event.setTitle(eventDto.getTitle());

        if (eventDto.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else event.setState(State.CANCELED);
        return createResponse(eventRepository.save(event));
    }


}
