package ru.practicum.ExploreWithMe.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsCompilationRepository extends JpaRepository<CompilationEvents, Long> {

    @Query("select ce.eventId " +
            "from CompilationEvents ce " +
            "where ce.compilationId = ?1 ")
    List<Long> findEventsId(Long compilationId);

    void deleteAllByCompilationId(Long compilationId);
}
