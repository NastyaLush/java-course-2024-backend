package edu.java.linkClients;

import java.time.OffsetDateTime;

public record LinkUpdateResponse(OffsetDateTime lastUpdate, String description) {
}
