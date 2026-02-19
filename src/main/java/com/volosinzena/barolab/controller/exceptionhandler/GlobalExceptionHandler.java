package com.volosinzena.barolab.controller.exceptionhandler;

import com.volosinzena.barolab.controller.dto.ErrorDto;
import com.volosinzena.barolab.exception.UserAlreadyExistsException;
import com.volosinzena.barolab.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        log.error("User not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(com.volosinzena.barolab.exception.CommentNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCommentNotFoundException(
            com.volosinzena.barolab.exception.CommentNotFoundException e) {
        log.error("Comment not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error("User already exists", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(com.volosinzena.barolab.exception.BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(
            com.volosinzena.barolab.exception.BadCredentialsException e) {
        log.error("Bad credentials", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(com.volosinzena.barolab.exception.UserBlockedException.class)
    public ResponseEntity<ErrorDto> handleUserBlockedException(
            com.volosinzena.barolab.exception.UserBlockedException e) {
        log.error("User is blocked", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Internal server error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
    }
}
