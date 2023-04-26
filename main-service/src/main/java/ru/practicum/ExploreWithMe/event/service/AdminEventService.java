package ru.practicum.ExploreWithMe.event.service;

import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getAll(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, int from, int size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest eventDto);
}
