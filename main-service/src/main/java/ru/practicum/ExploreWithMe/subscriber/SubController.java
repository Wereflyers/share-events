package ru.practicum.ExploreWithMe.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;
import ru.practicum.ExploreWithMe.statistics.StatisticService;
import ru.practicum.ExploreWithMe.user.dto.UserDtoWithSubAbility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/subs/{userId}")
@RequiredArgsConstructor
public class SubController {
    private final SubService subService;
    private final StatisticService st;

    @GetMapping
    public SubDto get(@PathVariable Long userId) {
        return subService.get(userId);
    }

    @PostMapping("/{followedId}")
    @ResponseStatus(HttpStatus.CREATED)
    public SubDto add(@PathVariable Long userId, @PathVariable Long followedId) {
        return subService.add(userId, followedId);
    }

    @DeleteMapping("/{followedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SubDto delete(@PathVariable Long userId, @PathVariable Long followedId) {
        return subService.delete(userId, followedId);
    }

    @PatchMapping
    public UserDtoWithSubAbility changeSubscribeAbility(@PathVariable long userId, @RequestParam boolean ability) {
        return subService.changeAbility(userId, ability);
    }

    @GetMapping("/{followedId}/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId, @PathVariable Long followedId, HttpServletRequest request) {
        st.addHit(request.getRequestURI(), request.getRemoteAddr());
        return subService.getEvents(userId, followedId);
    }
}
