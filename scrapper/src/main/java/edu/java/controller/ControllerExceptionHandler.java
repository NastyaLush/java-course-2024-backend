package edu.java.controller;

import com.example.exceptions.RateLimitingException;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.model.ApiErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.debug("send resource not found exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse alreadyExistException(AlreadyExistException ex, WebRequest request) {
        log.debug("send already exist exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {NotExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse notExistException(NotExistException ex, WebRequest request) {
        log.debug("send not exist exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.debug("send illegal argument exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {PSQLException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse psqlException(PSQLException ex, WebRequest request) {
        log.debug("send psql exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {RateLimitingException.class})
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorResponse psqlException(RateLimitingException ex, WebRequest request) {
        log.debug("send rate limit exception");
        return new ApiErrorResponse().exceptionMessage(ex.getMessage());
    }
}
