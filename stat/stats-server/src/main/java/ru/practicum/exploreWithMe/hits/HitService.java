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
import java.util.ArrayList;
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
        List<ViewStats> resp = new ArrayList<>();
        if (uris != null) {
            if (unique) {
                for (String s : uris) {
                    resp.add(hitRepository.countUniqueHitsWithUri(s, start, end));
                }
            } else for (String s : uris) {
                resp.add(hitRepository.countHitsWithUri(s, start, end));
            }
        } else if (unique) {
            resp = hitRepository.countUniqueHits(start, end);
        } else resp = hitRepository.countHits(start, end);
        return resp.stream()
                .map(HitMapper::toViewStatsDto)
                .sorted((o1, o2) -> o2.getHits() - o1.getHits())
                .collect(Collectors.toList());
    }
}
