package ru.practicum.ExploreWithMe.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ExploreWithMe.enums.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    Long category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
