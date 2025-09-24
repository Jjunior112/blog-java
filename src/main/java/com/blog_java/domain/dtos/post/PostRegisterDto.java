package com.blog_java.domain.dtos.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRegisterDto(
        @NotBlank
        @NotNull
        String userId,
        @NotBlank
        @NotNull
        String title,
        @NotBlank
        @NotNull
        String post,
        byte[] image) {
}
