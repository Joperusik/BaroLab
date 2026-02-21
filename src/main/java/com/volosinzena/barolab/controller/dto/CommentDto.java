package com.volosinzena.barolab.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;

    @JsonProperty("post_id")
    private String postId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("author_username")
    private String authorUsername;

    private String body;
    private Status status;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
