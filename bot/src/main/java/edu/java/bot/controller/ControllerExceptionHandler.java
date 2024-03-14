package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramException;
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
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "ресурса не существует")
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {TelegramException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "ошибка при работе с телеграммом")
    public ErrorMessage tgChatIdNotFoundException(TelegramException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }
}
