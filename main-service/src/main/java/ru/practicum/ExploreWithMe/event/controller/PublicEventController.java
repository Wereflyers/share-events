package ru.practicum.ExploreWithMe.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventFullDto;
import ru.practicum.ExploreWithMe.event.service.PublicEventService;
import ru.practicum.ExploreWithMe.statistics.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final PublicEventService eventService;
    private final StatisticService st;
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) String text, @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) Boolean paid, @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd, @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                     @RequestParam(required = false) String sort, @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(3000, 1, 1, 1, 1);
        if (rangeStart !=null && !rangeStart.isBlank()) {
            start = LocalDateTime.parse(rangeStart, DATE_PATTERN);
        }
        if (rangeEnd != null && !rangeEnd.isBlank()) {
            end = LocalDateTime.parse(rangeEnd, DATE_PATTERN);
        }
        st.addHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.getAll(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {
        st.addHit(request.getRequestURI(), request.getRemoteAddr());
        return eventService.get(eventId);
    }
}
