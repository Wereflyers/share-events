package ru.practicum.ExploreWithMe.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ErrorResponse {
    @JsonProperty("error")
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
