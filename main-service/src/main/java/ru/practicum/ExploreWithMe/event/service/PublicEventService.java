package ru.practicum.ExploreWithMe.event.service;

import ru.practicum.ExploreWithMe.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventFullDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    EventFullDto get(Long eventId);
}
