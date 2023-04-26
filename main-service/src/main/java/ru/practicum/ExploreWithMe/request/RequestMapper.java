package ru.practicum.ExploreWithMe.request;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.request.dto.ParticipationRequestDto;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getRequestId())
                .requester(request.getRequester())
                .event(request.getEvent())
                .created(request.getCreated())
                .status(request.getStatus().name())
                .build();
    }
}
