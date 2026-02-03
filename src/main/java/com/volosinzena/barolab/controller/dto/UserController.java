package com.volosinzena.barolab.controller.dto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class UserController {

    private HashMap<UUID, UserDto> userDtoHashMap = new HashMap<>();

}
