package ru.practicum.ExploreWithMe.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ExploreWithMe.user.User;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>, QuerydslPredicateExecutor<User> {

    List<Compilation> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}