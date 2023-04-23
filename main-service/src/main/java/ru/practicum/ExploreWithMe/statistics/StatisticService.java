package ru.practicum.ExploreWithMe.statistics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.stat.StatClient;

import java.time.LocalDateTime;
import java.util.List;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class StatisticService {
    private final StatClient client;

    public void addHit(String requestURI, String remoteAddr) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("ewm-main-service");
        hitDto.setUri(requestURI);
        hitDto.setIp(remoteAddr);
        hitDto.setTimestamp(LocalDateTime.now());
        client.postHit(hitDto);
    }

    public ResponseEntity<Object> getViews(List<String> uris) {
        return client.getViews(uris);
    }
}