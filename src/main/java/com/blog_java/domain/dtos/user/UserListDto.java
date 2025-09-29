package com.blog_java.domain.dtos.user;

import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.User;

import java.util.UUID;

public record UserListDto(Long id, String firstName, String lastName, String email, UserRole role,Boolean isEnabled) {

    public UserListDto(User user) {
        this(user.getId(), user.getFirstName(),user.getLastName(), user.getEmail(), user.getRole(),user.isEnabled());
    }
}