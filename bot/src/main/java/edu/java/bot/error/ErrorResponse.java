package edu.java.bot.error;

import java.io.Serializable;

public record ErrorResponse(String message) implements Serializable {
}
