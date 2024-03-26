package com.example.exceptions;

public class CustomWebClientException extends RuntimeException {
    public CustomWebClientException(String message) {
        super(message);
    }
}
