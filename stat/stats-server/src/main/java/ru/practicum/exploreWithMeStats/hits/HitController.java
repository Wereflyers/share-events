package ru.practicum.exploreWithMeStats.hits;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.Stat;
import ru.practicum.ExploreWithMe.dto.ViewStatsDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class HitController {
    private final HitService hitService;
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto saveHit(@RequestBody HitDto hit) {
        return hitService.post(hit);
    }

    @GetMapping("/views")
    public Stat getViews(@RequestParam List<String> uris) {
        return hitService.getViews(uris);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(name = "start") @NotNull String start, @RequestParam(name = "end") @NotNull String end,
                                       @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
                                       @RequestParam(name = "uris", required = false)  List<String> uris) {
        log.info("uris = " + uris);
        return hitService.get(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_PATTERN)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_PATTERN)), unique, uris);
    }
}
