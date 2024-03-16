package edu.java.linkClients;

import edu.java.bot.model.LinkUpdate;
import java.time.OffsetDateTime;

public record LinkUpdateResponse(OffsetDateTime lastUpdate, String description) {
}
