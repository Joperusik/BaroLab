package com.volosinzena.barolab.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID userId) {
        super("User with id " + userId.toString() + " not found");
    }

    public UserNotFoundException(String login) {
        super("User with login " + login + " not found");
    }
}
