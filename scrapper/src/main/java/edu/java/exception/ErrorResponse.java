package edu.java.exception;

import java.io.Serializable;

public record ErrorResponse(String message) implements Serializable {
}
