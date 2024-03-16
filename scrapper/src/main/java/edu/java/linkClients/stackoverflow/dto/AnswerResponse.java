package edu.java.linkClients.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record AnswerResponse(
    @JsonProperty("items") List<ItemResponse> items
) {
    public record ItemResponse(
        @JsonProperty("owner") Owner owner,
        @JsonProperty("creation_date") OffsetDateTime createdAt
    ) {
        public record Owner(@JsonProperty("link") String ownerLink) {

        }
    }
}
