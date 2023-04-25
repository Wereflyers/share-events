package ru.practicum.ExploreWithMe.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.Stat;
import ru.practicum.ExploreWithMe.stat.StatClient;

import java.time.LocalDateTime;
import java.util.List;

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

    public Stat getViews(Long id) {
        List<String> uris = List.of("/events/", "/events/" + id);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(client.getViews(uris).getBody(), Stat.class);
    }
}