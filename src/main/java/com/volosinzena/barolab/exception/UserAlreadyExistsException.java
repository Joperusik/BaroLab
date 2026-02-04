package com.volosinzena.barolab.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String login) {
        super("This login already exists: " + login);
    }
}
