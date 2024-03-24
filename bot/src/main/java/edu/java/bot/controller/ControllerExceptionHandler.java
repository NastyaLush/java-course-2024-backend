package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramException;
import edu.java.bot.error.ErrorResponse;
import edu.java.bot.exceptions.RateLimitingException;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "ресурса не существует")
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {TelegramException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "ошибка при работе с телеграммом")
    public ErrorResponse tgChatIdNotFoundException(TelegramException ex, WebRequest request) {
        return new ErrorResponse(ex.getMessage());
    }
    @ExceptionHandler(value = {RateLimitingException.class})
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse psqlException(RateLimitingException ex, WebRequest request) {
        log.debug("send rate limit exception");
        return new ErrorResponse(ex.getMessage());
    }
}
