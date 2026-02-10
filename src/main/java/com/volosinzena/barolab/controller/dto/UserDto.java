package com.volosinzena.barolab.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String login;
    private String email;
    @JsonIgnore
    private String password;
    private Status status;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
