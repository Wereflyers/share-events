package ru.practicum.ExploreWithMe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDtoWithSubAbility {
    Long id;
    String name;
    String email;
    boolean subscribe;
}
