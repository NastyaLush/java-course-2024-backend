package edu.java.repository.dto;

import java.time.ZonedDateTime;

public record UrlDTO(Integer id, String url, ZonedDateTime lastUpdate, ZonedDateTime lastCheck) {

}
