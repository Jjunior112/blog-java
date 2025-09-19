package com.blog_java.application.advices;

import com.blog_java.domain.dtos.exception.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;

@ControllerAdvice
public class HandlerExceptions extends RuntimeException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundException> errorNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ExceptionDto> errorResourceAccess(ResourceAccessException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> errorIllegaArg(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> errorNullPointer(NullPointerException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ExceptionDto genericException(Exception e) {
        return new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ExceptionDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)

    public ResponseEntity<ExceptionDto> error403(BadCredentialsException e)
    {
        return ResponseEntity.status(401).body(new ExceptionDto(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(errors.stream().map(FieldsErrors::new).toList());
    }

    private record FieldsErrors(String field, String message) {
        public FieldsErrors(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }


}