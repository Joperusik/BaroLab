package com.volosinzena.barolab.service.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Admin {

    private UUID id;
    private String login;
    private String password;
    private Status status;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
