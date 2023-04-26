package ru.practicum.ExploreWithMe.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ExploreWithMe.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    Long category;
    @Column(name = "created_on")
    @DateTimeFormat
    LocalDateTime createdOn;
    String description;
    @Column(name = "event_date")
    @DateTimeFormat
    LocalDateTime eventDate;
    Long initiator;
    Long location;
    Boolean paid;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Column(name = "published_on")
    @DateTimeFormat
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    String title;
}
