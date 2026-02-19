package com.volosinzena.barolab.service.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Comment {

    private UUID id;
    private UUID postId;
    private UUID userId;
    private String body;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}
