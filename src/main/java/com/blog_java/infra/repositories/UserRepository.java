package com.blog_java.infra.repositories;

import com.blog_java.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByEmail(String username);
}
