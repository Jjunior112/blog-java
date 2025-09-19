package com.blog_java.domain.dtos.exception;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public record ExceptionDto(LocalDateTime timestamp , int statusCode, String message) {
}
