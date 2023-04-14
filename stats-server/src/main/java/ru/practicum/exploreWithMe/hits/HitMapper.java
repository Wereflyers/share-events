package ru.practicum.exploreWithMe.hits;

import ru.practicum.exploreWithMe.hits.dto.EndpointHit;
import ru.practicum.exploreWithMe.hits.dto.Hit;

public class HitMapper {
    public static EndpointHit fromHitToEndpointHit(Hit hit) {
        return EndpointHit.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
