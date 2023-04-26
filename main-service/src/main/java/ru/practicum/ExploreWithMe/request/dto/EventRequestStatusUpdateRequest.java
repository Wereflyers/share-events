package ru.practicum.ExploreWithMe.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ExploreWithMe.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
