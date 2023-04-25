package ru.practicum.ExploreWithMe.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ExploreWithMe.event.model.Event;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "compilation_name", unique = true)
    String title;
    Boolean pinned;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    List<Event> events;
}
