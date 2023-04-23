package ru.practicum.ExploreWithMe.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Integer>, QuerydslPredicateExecutor<Stat> {

    List<Stat> findAllByUriIn(List<String> uris);
}
