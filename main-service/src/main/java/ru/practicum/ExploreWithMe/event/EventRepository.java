package ru.practicum.ExploreWithMe.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ExploreWithMe.enums.State;
import ru.practicum.ExploreWithMe.event.model.Event;
import ru.practicum.ExploreWithMe.event.model.EventShort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Event findByInitiatorAndId(Long userId, Long eventId);

    List<Event> findAllByInitiator(Long userId, PageRequest pageable);

    @Query("select e from Event e " +
            "where ((:users is null) or (e.initiator in :users)) " +
            "and ((:states is null) or (e.state in :states)) " +
            "and ((:categories is null) or (e.category in :categories)) " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    List<Event> getAllWithDateFilter(@Param(value = "users") List<Long> users, @Param("states") List<State> states, @Param("categories") List<Long> categories, @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e " +
            "where ((:text is null) or (upper(e.annotation) like %:text%) or (upper(e.description) like %:text%)) " +
            "and ((:paid is null) or (e.paid = :paid)) " +
            "and ((:categories is null) or (e.category in :categories)) " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    List<Event> searchWithoutSort(@Param("text") String text, @Param("categories") List<Long> categories, @Param("paid") Boolean paid,
                       @Param("rangeStart") LocalDateTime rangeStart, @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select e from Event e " +
            "where ((:text is null) or (upper(e.annotation) like %:text%) or (upper(e.description) like %:text%)) " +
            "and ((:paid is null) or (e.paid = :paid)) " +
            "and ((:categories is null) or (e.category in :categories)) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "order by e.eventDate")
    List<Event> searchWithSortByDate(@Param("text") String text, @Param("categories") List<Long> categories, @Param("paid") Boolean paid,
                                     @Param("rangeStart") LocalDateTime rangeStart, @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest pageRequest);

    @Query("select new ru.practicum.ExploreWithMe.event.model.EventShort(e.annotation, e.category, e.eventDate, e.id, e.initiator, e.paid, e.title) " +
            "from Event e " +
            "where e.id in ?1 ")
    List<EventShort> findEventsShort(Set<Long> eventIds);

    List<Event> findAllByCategory(long id);

    List<Event> findAllByInitiatorAndStateAndEventDateAfter(Long userId, State state, LocalDateTime eventDate);
}
