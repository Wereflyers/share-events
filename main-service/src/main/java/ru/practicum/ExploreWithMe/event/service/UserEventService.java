package ru.practicum.ExploreWithMe.event.service;

import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.NewEventDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface UserEventService {

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto get(Long userId, Long eventId);

    List<EventFullDto> getAllForUser(Long userId, int from, int size);
    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);
}
