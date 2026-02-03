package com.volosinzena.barolab.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String id;
    private String post_id;
    private String user_id;
    private String text;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;


}
