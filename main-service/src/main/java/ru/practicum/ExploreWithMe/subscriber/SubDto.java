package ru.practicum.ExploreWithMe.subscriber;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubDto {
    UserShortDto user;
    List<Long> subscriptions;
    int subscribers;
}
