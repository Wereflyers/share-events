package ru.practicum.ExploreWithMe.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.NewEventDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventUserRequest;
import ru.practicum.ExploreWithMe.event.service.UserEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class UserEventController {
    private final UserEventService userEventService;

    @Autowired
    public UserEventController(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    /**
     * Добавление события
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable long userId, @RequestBody @Valid NewEventDto eventDto) {
        return userEventService.create(userId, eventDto);
    }

    /** Изменение события
     * @param userId - пользователь
     * @param eventId - событие
     * @return EventFullDto
     */
    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long userId, @PathVariable Long eventId, @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return userEventService.update(userId, eventId, updateEventUserRequest);
    }

    /**
     * Получение информации о событии, добавленном текущим пользователем
     * @param userId - пользователь
     * @param eventId - событие
     * @return EventFullDto
     */
    @GetMapping("/{eventId}")
    public EventFullDto get(@PathVariable long userId, @PathVariable Long eventId) {
        return userEventService.get(userId, eventId);
    }

    /**
     * Получение событий, добавленных текущим пользователем
     * @param userId - пользователь
     * @return List
     */
    @GetMapping
    public List<EventFullDto> getAll(@PathVariable long userId, @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        return userEventService.getAllForUser(userId, from, size);
    }
}
