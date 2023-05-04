package ru.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.dto.UpdateEventAdminRequest;
import ru.practicum.ExploreWithMe.event.service.AdminEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users, @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories, @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd, @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(3000, 1, 1, 1, 1);
        if (rangeStart != null && !rangeStart.isBlank()) {
            start = LocalDateTime.parse(rangeStart, DATE_PATTERN);
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            end = LocalDateTime.parse(rangeEnd, DATE_PATTERN);
        }
        return adminEventService.getAll(users, states, categories, start, end, from, size);
    }

    @PatchMapping("{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest eventDto) {
        return adminEventService.update(eventId, eventDto);
    }
}
