package ru.practicum.exploreWithMeStats.hits;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMeStats.hits.model.EndpointHit;
import ru.practicum.exploreWithMeStats.hits.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Integer>, QuerydslPredicateExecutor<EndpointHit> {

    @Query(value = "select new ru.practicum.exploreWithMeStats.hits.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<ViewStats> countHits(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.exploreWithMeStats.hits.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 and hit.uri in ?3 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<ViewStats> countHitsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.exploreWithMeStats.hits.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 and hit.uri in ?3 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<ViewStats> countUniqueHitsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.exploreWithMeStats.hits.model.ViewStats(hit.app, hit.uri, count(distinct hit.ip)) " +
            "from EndpointHit as hit " +
            "where timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.ip) desc")
    List<ViewStats> countUniqueHits(LocalDateTime start, LocalDateTime end);

    List<EndpointHit> findAllByUriIn(List<String> uri);
}
