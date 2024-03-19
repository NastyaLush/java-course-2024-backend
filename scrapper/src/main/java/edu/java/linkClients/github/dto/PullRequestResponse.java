package edu.java.linkClients.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record PullRequestResponse(
        @JsonProperty("id") Integer id,
        @JsonProperty("html_url") String url,
        @JsonProperty("title") String title,
        @JsonProperty("body") String body,
        @JsonProperty("created_at") OffsetDateTime createdAt,
        @JsonProperty("user") UserResponse userResponse
) {
    public record UserResponse(
            @JsonProperty("login") String login,
            @JsonProperty("html_url") String url
    ) {
    }
}
