package ru.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.service.PublicEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class PublicEventController {
    private final PublicEventService eventService;
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) Boolean paid, @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd, @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                     @RequestParam(required = false) String sort, @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        //TODO сохранить в сервис статистики
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;
        if (rangeStart !=null && !rangeStart.isBlank()) {
            start = LocalDateTime.parse(rangeStart, DATE_PATTERN);
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            end = LocalDateTime.parse(rangeEnd, DATE_PATTERN);
        }
        return eventService.getAll(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable(name = "id") Long eventId) {
        //TODO сохранить в сервис статистики
        return eventService.get(eventId);
    }
}
