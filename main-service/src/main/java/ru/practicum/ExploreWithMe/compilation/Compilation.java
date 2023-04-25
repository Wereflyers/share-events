package ru.practicum.ExploreWithMe.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

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
    @ElementCollection
    @CollectionTable(name = "events_compilations", joinColumns = @JoinColumn(name = "compilation_id"))
    @Column(name = "event_id")
    Set<Long> events;
}
