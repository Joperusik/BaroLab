package com.volosinzena.barolab.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private String id;
    private String login;
    private String status;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;
}
