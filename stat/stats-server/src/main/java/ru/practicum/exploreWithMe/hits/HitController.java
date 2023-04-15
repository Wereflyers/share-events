package ru.practicum.exploreWithMe.hits;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.hits.model.EndpointHit;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class HitController {
    private final HitService hitService;

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody HitDto hit) {
        return hitService.post(hit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @NotNull String start, @RequestParam(name = "end") @NotNull String end,
                                       @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
                                       @RequestParam(name = "uris", required = false)  String[] uris) {
        log.info(Arrays.toString(uris));
        return hitService.get(LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), unique, uris);
    }
}
