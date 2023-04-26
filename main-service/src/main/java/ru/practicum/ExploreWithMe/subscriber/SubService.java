package ru.practicum.ExploreWithMe.subscriber;

import ru.practicum.ExploreWithMe.event.dto.EventShortDto;

import java.util.List;

public interface SubService {
    SubDto add(Long subId, Long userId);

    SubDto delete(long subId, Long userId);

    SubDto get(long userId);

    List<EventShortDto> getEvents(Long subId, Long userId);
}
