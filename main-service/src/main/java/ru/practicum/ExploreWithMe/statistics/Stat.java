package ru.practicum.ExploreWithMe.statistics;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @DateTimeFormat
    private LocalDateTime timestamp;
}
