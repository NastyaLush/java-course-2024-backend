package edu.java.entity;

import java.time.OffsetDateTime;

public record UrlInput(String url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
}
