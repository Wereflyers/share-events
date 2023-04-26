package ru.practicum.ExploreWithMe.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.category.dto.NewCategoryDto;

import javax.validation.Valid;
import java.util.List;

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
    public CategoryDto add(@RequestBody @Valid NewCategoryDto categoryDto) {
        return categoryService.add(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryDto delete(@PathVariable long catId) {
        return categoryService.delete(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto get(@PathVariable long catId) {
        return categoryService.get(catId);
    }
}
