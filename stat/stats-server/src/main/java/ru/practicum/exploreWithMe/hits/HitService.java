package ru.practicum.exploreWithMe.hits;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.hits.model.EndpointHit;
import ru.practicum.exploreWithMe.hits.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class HitService {
    private final HitRepository hitRepository;

    @Autowired
    public HitService(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Transactional
    public EndpointHit post(HitDto hit) {
        return hitRepository.save(HitMapper.fromHitToEndpointHit(hit));
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, Boolean unique, String[] uris) {
        List<ViewStats> resp = null;
        if (uris != null) {
            if (unique) {
                resp = hitRepository.countUniqueHitsWithUris(uris, start, end);
            }
            resp = hitRepository.countHitsWithUris(uris, start, end);
        }
        if (unique) {
            resp = hitRepository.countUniqueHits(start, end);
        }
        resp = hitRepository.countHits(start, end);
        return resp.stream()
                .map(HitMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }
}
