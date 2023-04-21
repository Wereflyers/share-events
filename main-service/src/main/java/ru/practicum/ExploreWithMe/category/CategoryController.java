package ru.practicum.ExploreWithMe.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto update(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(catId, categoryDto);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.add(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryDto delete(@PathVariable long catId) {
        return categoryService.delete(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAll(@RequestParam(required = false, defaultValue = "0") int from, @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto get(@PathVariable long catId) {
        return categoryService.get(catId);
    }

    @GetMapping("/some/path/{id}")
    public void logIPAndPath(@PathVariable long id, HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
    }
}
