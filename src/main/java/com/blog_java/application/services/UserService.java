package com.blog_java.application.services;

import com.blog_java.domain.dtos.user.UserRegisterDto;
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
    public User createUser(UserRegisterDto userRegisterDto)
    {
        if(userRepository.findByEmail(userRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());

        User user = new User(userRegisterDto.firstName(), userRegisterDto.lastName(), userRegisterDto.email(),encryptedPassword, UserRole.USER);

        return userRepository.save(user);
    }

    @Transactional
    public User createUserAdmin(UserRegisterDto userRegisterDto)
    {
        if(userRepository.findByEmail(userRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());

        User user = new User(userRegisterDto.firstName(), userRegisterDto.lastName(), userRegisterDto.email(),encryptedPassword, UserRole.ADMIN);

        return userRepository.save(user);
    }

    @Transactional
    public User createUserModerator(UserRegisterDto userRegisterDto)
    {
        if(userRepository.findByEmail(userRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());

        User user = new User(userRegisterDto.firstName(), userRegisterDto.lastName(), userRegisterDto.email(),encryptedPassword, UserRole.MODERATOR);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        var user = userRepository.findById(id);

        if(user.isEmpty())
        {
            throw new NullPointerException("User not found!");
        }

        var userEntity = user.get();

        return userEntity;
    }

    public boolean existUser(String email) {
        var user = userRepository.findByEmail(email);

        return user != null;
    }




}
