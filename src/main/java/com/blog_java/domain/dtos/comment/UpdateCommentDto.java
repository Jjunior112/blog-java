package com.blog_java.domain.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentDto(
        @NotBlank
        @NotNull
        String comment) {
}
