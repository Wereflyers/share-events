package ru.practicum.ExploreWithMe.subscriber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRepository extends JpaRepository<Subscriber, Long>, QuerydslPredicateExecutor<Subscriber> {

    List<Subscriber> findAllBySubId(Long subId);

    int countAllByUserId(Long userId);

    Boolean existsBySubIdAndUserId(Long subId, Long userId);

    void deleteBySubIdAndUserId(Long subId, Long userId);
}