package ru.practicum.ExploreWithMe.stat;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.dto.HitDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Controller("client")
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@RequestBody @Valid HitDto hit) {
        return statClient.postHit(hit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam(name = "start") @NotNull String start, @RequestParam(name = "end") @NotNull String end,
                                           @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
                                           @RequestParam(name = "uris", required = false)  String[] uris) {
        return statClient.getStats(start, end, uris, unique);
    }
}
