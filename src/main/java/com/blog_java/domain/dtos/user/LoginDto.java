package com.blog_java.domain.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull
        @NotBlank
        @Email
        String email,
        @NotNull
        @NotBlank
        String password) {
}
