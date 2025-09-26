package com.blog_java.domain.dtos.user;


import java.util.UUID;

public record JwtDto(Long userId, String token, String role) {
}
