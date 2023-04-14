package ru.practicum.exploreWithMe.hits.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hit")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    @DateTimeFormat
    private LocalDateTime timestamp;
}
