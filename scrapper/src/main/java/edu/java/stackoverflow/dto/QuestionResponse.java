package edu.java.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;


public record QuestionResponse(@JsonProperty("items") List<ItemResponse> items) {
    record ItemResponse(@JsonProperty("question_id") Long id, @JsonProperty("title") String title,
                        @JsonProperty("link") String link,
                        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
                        @JsonProperty("creation_date") OffsetDateTime creationDate,
                        @JsonProperty("last_edit_date") OffsetDateTime lastEditDate
    ) {

    }
}
