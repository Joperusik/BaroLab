package com.volosinzena.barolab.service.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Post {

    private UUID id;
    private UUID userId;
    private Integer rating;
    private String status;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
