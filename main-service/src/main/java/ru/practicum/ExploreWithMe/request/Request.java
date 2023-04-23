package ru.practicum.ExploreWithMe.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ExploreWithMe.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "EVENT_REQUESTS")
public class Request {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long requestId;
    Long event;
    @Column(name = "requester")
    Long requester;
    @Column(name = "created")
    @DateTimeFormat
    LocalDateTime created;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
