package ru.practicum.ExploreWithMe.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ExploreWithMe.user.dto.UserShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SubDto {
    UserShortDto user;
    List<Long> subscriptions;
    int subscribers;
}
