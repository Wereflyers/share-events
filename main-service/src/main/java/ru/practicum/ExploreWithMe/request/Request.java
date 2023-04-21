package ru.practicum.ExploreWithMe.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ExploreWithMe.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long requestId;
    @Column(name = "event_id")
    Long event;
    Long requester;
    LocalDateTime created;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
