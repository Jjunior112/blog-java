package com.blog_java.infra.security;

import com.blog_java.domain.dtos.user.UserRegisterDto;
import com.blog_java.application.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitialUserCreator {

    @Value("${api.security.firstName}")
    private String firstName;

    @Value("${api.security.lastName}")
    private String lastName;

    @Value("${api.security.email}")
    private String email;

    @Value("${api.security.password}")
    private String password;

    private UserService userService;

    public InitialUserCreator(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialUser() {
        if (!userService.existUser(email)) {
            UserRegisterDto register = new UserRegisterDto(
                    firstName,
                    lastName,
                    email,
                    password
            );
            userService.createUserAdmin(register);
        }

    }
}