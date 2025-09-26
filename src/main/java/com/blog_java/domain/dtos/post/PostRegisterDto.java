package com.blog_java.domain.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PostRegisterDto(
        @NotNull
        Long userId,
        @NotBlank
        @NotNull
        String title,
        @NotBlank
        @NotNull
        String post,
        byte[] imageBase64) {
}
