package com.volosinzena.barolab.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String login;
    private String email;
    private String password;
    private Status status;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
