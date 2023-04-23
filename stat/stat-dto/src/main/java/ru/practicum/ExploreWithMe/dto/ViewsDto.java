package ru.practicum.ExploreWithMe.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewsDto {
    List<String> uris;
    int views;
}
