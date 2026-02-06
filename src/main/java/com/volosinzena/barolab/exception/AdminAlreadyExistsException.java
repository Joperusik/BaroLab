package com.volosinzena.barolab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdminAlreadyExistsException extends RuntimeException {
    public AdminAlreadyExistsException(String login) {
        super("Admin with login " + login + " already exists");
    }
}
