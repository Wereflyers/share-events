package ru.practicum.exploreWithMe.hits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.hits.dto.EndpointHit;
import ru.practicum.exploreWithMe.hits.dto.Hit;
import ru.practicum.exploreWithMe.hits.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping
public class HitController {
    private final HitService hitService;

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody Hit hit) {
        return hitService.post(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start") String start, @RequestParam(name = "end") String end,
                                    @RequestParam(name = "unique", required = false, defaultValue = "false") Boolean unique,
                                    @RequestParam(name = "uris", required = false)  String[] uris) {
        return hitService.get(LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), unique, uris);
    }
}
