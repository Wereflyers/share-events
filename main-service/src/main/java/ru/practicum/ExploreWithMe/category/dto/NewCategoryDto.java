package ru.practicum.ExploreWithMe.category.dto;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank
    @NotNull
    private String name;
}
