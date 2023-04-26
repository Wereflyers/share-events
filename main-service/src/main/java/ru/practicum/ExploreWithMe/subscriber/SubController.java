package ru.practicum.ExploreWithMe.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;
import ru.practicum.ExploreWithMe.statistics.StatisticService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/sub")
@Validated
public class SubController {
    private final SubService subService;
    private final StatisticService st;

    @Autowired
    public SubController(SubService subService, StatisticService st) {
        this.subService = subService;
        this.st = st;
    }

    @GetMapping("/{subId}")
    public SubDto get(@PathVariable Long subId) {
        return subService.get(subId);
    }

    @PostMapping("/{subId}/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubDto add(@PathVariable Long subId, @PathVariable Long userId) {
        return subService.add(subId, userId);
    }

    @DeleteMapping("/{subId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SubDto delete(@PathVariable Long subId, @PathVariable Long userId) {
        return subService.delete(subId, userId);
    }

    @GetMapping("/{subId}/events/{userId}")
    public List<EventShortDto> getEvents(@PathVariable Long subId, @PathVariable Long userId, HttpServletRequest request) {
        st.addHit(request.getRequestURI(), request.getRemoteAddr());
        return subService.getEvents(subId, userId);
    }
}
