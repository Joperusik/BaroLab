package com.volosinzena.barolab.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("author_username")
    private String authorUsername;

    private Integer rating;
    private Status status;
    private String title;
    private String content;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
