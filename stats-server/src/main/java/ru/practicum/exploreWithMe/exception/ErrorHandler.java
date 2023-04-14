package ru.practicum.exploreWithMe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNullException(final NullPointerException e) {
        log.info("NullPointerException " + Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(final Exception e) {
        log.info("Exception " + Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(final DuplicateException e) {
        log.info("DuplicateException " + Arrays.toString(e.getStackTrace()));
        return new ErrorResponse(e.getMessage());
    }
}
