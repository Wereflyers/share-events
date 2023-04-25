package ru.practicum.ExploreWithMe.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank
    private String name;
}
