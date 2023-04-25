package ru.practicum.ExploreWithMe.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class NewCategoryDto {
    @NotBlank
    private String name;
}
