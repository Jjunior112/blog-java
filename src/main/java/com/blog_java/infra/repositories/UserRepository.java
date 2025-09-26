package com.blog_java.infra.repositories;

import com.blog_java.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String username);
}
