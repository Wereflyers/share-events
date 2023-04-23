package ru.practicum.ExploreWithMe.statistics;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.dto.HitDto;

@UtilityClass
public class StatMapper {
    public static Stat fromHitToStat(HitDto hit) {
        return Stat.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
