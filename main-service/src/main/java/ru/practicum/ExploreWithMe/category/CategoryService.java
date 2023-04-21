package ru.practicum.ExploreWithMe.category;


import ru.practicum.ExploreWithMe.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto update(long id, CategoryDto categoryDto);
    CategoryDto delete(long id);
    CategoryDto add(CategoryDto categoryDto);
    List<CategoryDto> getAll(int from, int size);
    CategoryDto get(long id);
}
