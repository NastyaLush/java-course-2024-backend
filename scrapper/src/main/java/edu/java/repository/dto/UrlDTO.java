package edu.java.repository.dto;

import lombok.Builder;

import java.time.ZonedDateTime;
@Builder
public record UrlDTO(Integer id, String url, ZonedDateTime lastUpdate, ZonedDateTime lastCheck) {

}
