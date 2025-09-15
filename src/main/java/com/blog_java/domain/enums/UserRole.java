package com.blog_java.domain.enums;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    CLIENT("ROLE_CLIENT");
    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}