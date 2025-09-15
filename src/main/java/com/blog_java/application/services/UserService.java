package com.blog_java.application.services;

import com.blog_java.domain.dtos.user.ClientRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.User;
import com.blog_java.infra.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Transactional
    public User createClientUser(ClientRegisterDto clientRegisterDto)
    {
        if(userRepository.findByEmail(clientRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(clientRegisterDto.password());

        User user = new User(clientRegisterDto.firstName(),clientRegisterDto.lastName(), clientRegisterDto.email(),encryptedPassword, UserRole.CLIENT);

        return userRepository.save(user);
    }

    public User findById(String id) {
        var user = userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new NullPointerException("User not found!");
        }

        var userEntity = user.get();

        return userEntity;
    }




}
