package com.volosinzena.barolab.exception;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException(String login) {
        super("User is blocked: " + login);
    }
}
