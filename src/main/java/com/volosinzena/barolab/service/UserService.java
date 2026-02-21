package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User signUp(String login, String email, String username, String password);

    User getUserById(UUID userId);

    List<User> getAllUsers();

    User activateUser(UUID userId);

    User blockUser(UUID userId);

    User updateRole(UUID userId, com.volosinzena.barolab.controller.dto.Role role);

    User login(String login, String password);
}
