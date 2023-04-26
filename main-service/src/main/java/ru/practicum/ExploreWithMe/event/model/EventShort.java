package ru.practicum.ExploreWithMe.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EventShort {
    String annotation;
    Long category;
    Integer confirmedRequests;
    LocalDateTime eventDate;
    Long id;
    Long initiator;
    Boolean paid;
    String title;
    Integer views;

    public EventShort(String annotation, Long category, LocalDateTime eventDate, Long id, Long initiator, Boolean paid, String title) {
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.paid = paid;
        this.title = title;
    }
}
