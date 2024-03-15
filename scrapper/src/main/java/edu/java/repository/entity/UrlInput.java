package edu.java.repository.entity;

import java.time.OffsetDateTime;

public record UrlInput(String url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
}
