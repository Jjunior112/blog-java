package com.blog_java.domain.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CommentRegisterDto(
        @NotNull
        @NotBlank
        String postId,
        @NotNull
        @NotBlank
        String comment) {
}
