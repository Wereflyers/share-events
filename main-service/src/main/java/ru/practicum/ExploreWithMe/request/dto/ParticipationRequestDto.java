package ru.practicum.ExploreWithMe.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    Long id;
    Long event;
    Long requester;
    LocalDateTime created;
    String status;
}
