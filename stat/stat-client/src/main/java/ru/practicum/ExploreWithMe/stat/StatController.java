package ru.practicum.ExploreWithMe.stat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.dto.HitDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@Controller("client")
@RequestMapping
@RequiredArgsConstructor
public class StatController {
    private final StatClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@Valid @RequestBody HitDto hitDto) {
        log.info("Сохранение информации о том, что к эндпоинту был запрос");
        return statsClient.postHit(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                      @RequestParam(defaultValue = "") List<String> uris,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Получен запрос на получение статистики по посещениям");
        return statsClient.getStats(start, end, uris, unique);
    }
}
