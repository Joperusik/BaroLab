package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        TokenResponseDto tokenResponseDto = new TokenResponseDto();

        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> createUser(@RequestBody SignUpRequestDto signUpRequestDto) {

        UserDto userDto = new UserDto();

        userDto.setLogin(signUpRequestDto.getLogin());
        userDto.setEmail(signUpRequestDto.getEmail());

        Instant now = Instant.now();

        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);
        userDto.setRole(String.valueOf(Role.USER));
        userDto.setStatus(String.valueOf(Status.ACTIVE));

        return ResponseEntity.ok(userDto);
    }
}
