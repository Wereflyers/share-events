package ru.practicum.ExploreWithMe.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events_compilations")
public class CompilationEvents {
    @Id
    @Column(name = "events_compilations_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long ceId;
    @Column(name = "event_id")
    Long eventId;
    @Column(name = "compilation_id")
    Long compilationId;
}
