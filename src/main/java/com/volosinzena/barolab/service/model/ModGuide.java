package com.volosinzena.barolab.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModGuide {
    private UUID id;
    private Long modId;
    private String title;
    private String content;
    private User author;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
}
