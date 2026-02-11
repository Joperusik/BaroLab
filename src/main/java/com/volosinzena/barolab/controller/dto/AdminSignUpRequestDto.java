package com.volosinzena.barolab.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignUpRequestDto {
    private String login;
    private String password;
}

