package ru.practicum.ExploreWithMe.statistics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ExploreWithMe.dto.HitDto;
import ru.practicum.ExploreWithMe.dto.ViewsDto;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StatService {
    private final StatRepository statRepository;

    @Autowired
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Transactional
    public void post(HitDto hit) {
        statRepository.save(StatMapper.fromHitToStat(hit));
    }

    public ViewsDto count(List<String> uris) {
        return new ViewsDto(uris, statRepository.findAllByUriIn(uris).size());
    }
}
