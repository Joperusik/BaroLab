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

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handlerUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());

        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handlerUserNotFoundException(UserNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());

        log.error(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
