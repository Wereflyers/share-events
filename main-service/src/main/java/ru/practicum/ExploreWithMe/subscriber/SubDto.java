package ru.practicum.ExploreWithMe.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubDto {
    UserShortDto user;
    int subscriptions;
    int subscribers;
}
