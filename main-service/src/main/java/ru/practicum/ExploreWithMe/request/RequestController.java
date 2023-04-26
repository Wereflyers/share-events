package ru.practicum.ExploreWithMe.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ExploreWithMe.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ExploreWithMe.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto add(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.add(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        return requestService.getAll(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancel(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        return requestService.getUserRequests(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateState(@PathVariable Long userId, @PathVariable Long eventId,
                                                      @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        return requestService.update(userId, eventId, updateRequest);
    }
}
