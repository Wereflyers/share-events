package ru.practicum.ExploreWithMe.compilation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    Set<Long> events;
    Boolean pinned;
    String title;
}
