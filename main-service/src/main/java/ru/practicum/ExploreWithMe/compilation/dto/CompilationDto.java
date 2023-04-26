package ru.practicum.ExploreWithMe.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ExploreWithMe.event.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompilationDto {
    private Long id;
    private boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
