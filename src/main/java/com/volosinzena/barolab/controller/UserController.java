package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.service.TokenService;
import com.volosinzena.barolab.controller.dto.LoginRequestDto;
import com.volosinzena.barolab.controller.dto.SignUpRequestDto;
import com.volosinzena.barolab.controller.dto.TokenResponseDto;
import com.volosinzena.barolab.controller.dto.UserDto;
import com.volosinzena.barolab.mapper.UserMapper;
import com.volosinzena.barolab.service.UserService;
import com.volosinzena.barolab.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        User user = userService.signUp(
                signUpRequestDto.getLogin(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword());

        String token = tokenService.generateToken(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.login(
                loginRequestDto.getLogin(),
                loginRequestDto.getPassword());

        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {

        List<User> userList = userService.getAllUsers();

        List<UserDto> userDtoList = userList.stream().map(userMapper::toDto).toList();

        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {

        User user = userService.getUserById(userId);

        UserDto userDto = userMapper.toDto(user);

        return ResponseEntity.ok(userDto);

    }

    @PutMapping("/user/{userId}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable UUID userId) {

        User user = userService.activateUser(userId);

        UserDto userDto = userMapper.toDto(user);

        return ResponseEntity.ok(userDto);

    }

    @PutMapping("/user/{userId}/block")
    public ResponseEntity<UserDto> blockUser(@PathVariable UUID userId) {

        User user = userService.blockUser(userId);

        UserDto userDto = userMapper.toDto(user);

        return ResponseEntity.ok(userDto);

    }

}
