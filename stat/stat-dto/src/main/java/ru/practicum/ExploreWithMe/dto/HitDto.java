package ru.practicum.ExploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class HitDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
