package com.volosinzena.barolab.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModPostDto {
    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("author_username")
    private String authorUsername;

    private Integer rating;

    @JsonProperty("my_vote")
    private VoteValue myVote;

    private Status status;
    private String title;
    private String content;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("external_id")
    private Long externalId;

    @JsonProperty("popularity")
    private Integer popularity;
}
