package ru.practicum.ExploreWithMe.category;

import lombok.experimental.UtilityClass;
import ru.practicum.ExploreWithMe.category.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
