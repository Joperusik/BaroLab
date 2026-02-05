package com.volosinzena.barolab.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private UUID id;
    private String login;
    private String status;
    private String role;
    private String password;
    private Instant createdAt;
    private Instant updatedAt;
}
