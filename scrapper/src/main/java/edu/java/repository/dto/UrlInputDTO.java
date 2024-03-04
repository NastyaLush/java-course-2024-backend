package edu.java.repository.dto;

import java.time.ZonedDateTime;

public record UrlInputDTO (String url, ZonedDateTime lastUpdate, ZonedDateTime lastCheck){
}
