package ru.practicum.ExploreWithMe.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.compilation.dto.CompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.NewCompilationDto;
import ru.practicum.ExploreWithMe.compilation.dto.UpdateCompilationRequest;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@Validated
public class CompilationController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.add(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        compilationService.delete(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@PathVariable Long compId, @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.update(compId, updateCompilationRequest);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        return compilationService.get(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned, @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return compilationService.getAll(pinned, from, size);
    }
}
