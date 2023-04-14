package ru.practicum.exploreWithMe.hits;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.hits.dto.EndpointHit;
import ru.practicum.exploreWithMe.hits.dto.Hit;
import ru.practicum.exploreWithMe.hits.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

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
    public EndpointHit post(Hit hit) {
        return hitRepository.save(HitMapper.fromHitToEndpointHit(hit));
    }

    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, Boolean unique, String[] uris) {
        if (uris != null) {
            if (unique) {
                log.info("1");
                return hitRepository.countUniqueHitsWithUris(uris, start, end);
            }
            log.info("2");
            return hitRepository.countHitsWithUris(uris, start, end);
        }
        if (unique) {
            log.info("3");
            return hitRepository.countUniqueHits(start, end);
        }
        log.info("4");
        return hitRepository.countHits(start, end);
    }
}
