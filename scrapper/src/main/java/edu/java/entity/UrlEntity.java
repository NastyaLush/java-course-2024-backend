package edu.java.entity;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record UrlEntity(Long id, String url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {

}
