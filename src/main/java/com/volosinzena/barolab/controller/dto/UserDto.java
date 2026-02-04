package com.volosinzena.barolab.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;
    private String login;
    private String email;
    private String password;
    private String status;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;
}
