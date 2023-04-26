package ru.practicum.ExploreWithMe.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LocationDto {
    private Float lat;
    private Float lon;
}
