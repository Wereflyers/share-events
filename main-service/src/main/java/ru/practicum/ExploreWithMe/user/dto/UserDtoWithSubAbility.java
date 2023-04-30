package ru.practicum.ExploreWithMe.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoWithSubAbility {
    Long id;
    String name;
    String email;
    boolean subscribe;
}
