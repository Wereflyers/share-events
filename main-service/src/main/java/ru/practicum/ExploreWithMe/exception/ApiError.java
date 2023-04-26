package ru.practicum.ExploreWithMe.exception;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private final String errors;
    private final String message;
    private final String status;
    private final String reason;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ApiError(String status, String message, String reason, String errors) {
        this.errors = errors;
        this.message = message;
        this.status = status;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }
}
