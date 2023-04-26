package ru.practicum.ExploreWithMe.request;

import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ExploreWithMe.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto add(Long userId, Long eventId);

    List<ParticipationRequestDto> getAll(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getUserRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult update(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
