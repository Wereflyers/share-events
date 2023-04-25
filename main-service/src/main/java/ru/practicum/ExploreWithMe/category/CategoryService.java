package ru.practicum.ExploreWithMe.category;


import ru.practicum.ExploreWithMe.category.dto.CategoryDto;
import ru.practicum.ExploreWithMe.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto update(long id, CategoryDto categoryDto);
    CategoryDto delete(long id);
    CategoryDto add(NewCategoryDto categoryDto);
    List<CategoryDto> getAll(int from, int size);
    CategoryDto get(long id);
}
