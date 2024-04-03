package edu.java.client.linkClients.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record IssueResponse(
        @JsonProperty("id") Integer id,
        @JsonProperty("html_url") String htmlUtl,
        @JsonProperty("created_at") OffsetDateTime createdAt
) {
}
