package edu.java.controller;

import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {AlreadyExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "уже зарегестрирован")
    public ErrorMessage chatAlreadyRegisteredException(AlreadyExistException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {NotExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "не существует")
    public ErrorMessage chatAlreadyRegisteredException(NotExistException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "невалидные аргументы")
    public ErrorMessage illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }
}
