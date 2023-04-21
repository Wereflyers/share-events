package ru.practicum.ExploreWithMe.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ExploreWithMe.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Request> {
    List<Request> findAllByRequester(Long userId);
    Optional<Request> findByRequesterAndRequestId(Long userId, Long requestId);
    List<Request> findAllByRequesterAndEvent(Long userId, Long eventId);
    List<Request> findAllByStatusAndEvent(RequestStatus state, Long eventId);
}
