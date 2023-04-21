package ru.practicum.ExploreWithMe.compilation;


import ru.practicum.ExploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.NewCompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto add(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    CompilationDto get(Long compId);

    List<CompilationDto> getAll(Boolean pinned, int from, int size);
}
