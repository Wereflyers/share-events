package ru.practicum.exploreWithMe.hits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.hits.model.EndpointHit;
import ru.practicum.exploreWithMe.hits.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Integer>, QuerydslPredicateExecutor<EndpointHit> {

    @Query(value = "select new ru.practicum.exploreWithMe.hits.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app ")
    List<ViewStats> countHits(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.exploreWithMe.hits.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.uri in ?1 and " +
            "timestamp between ?2 and ?3 " +
            "group by hit.uri, hit.app ")
    List<ViewStats> countHitsWithUris(String[] uris, LocalDateTime start, LocalDateTime end);
    @Query(value = "select new ru.practicum.exploreWithMe.hits.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where hit.uri in ?1 and " +
            "timestamp between ?2 and ?3 " +
            "group by hit.uri, hit.app ")
    List<ViewStats> countUniqueHitsWithUris(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.exploreWithMe.hits.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app ")
    List<ViewStats> countUniqueHits(LocalDateTime start, LocalDateTime end);
}
