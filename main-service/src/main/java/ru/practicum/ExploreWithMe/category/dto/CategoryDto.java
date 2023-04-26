package ru.practicum.ExploreWithMe.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank
    private String name;
}
