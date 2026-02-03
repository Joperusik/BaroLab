package com.volosinzena.barolab.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String login;
    private String email;
    private String status;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;
}
