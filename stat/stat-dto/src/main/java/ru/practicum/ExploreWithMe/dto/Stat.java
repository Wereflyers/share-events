package ru.practicum.ExploreWithMe.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Stat {
    private List<String> uri;
    private int hits;
}
