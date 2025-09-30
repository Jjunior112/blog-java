package com.blog_java.application.services;

import com.blog_java.domain.dtos.user.UserRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.ConfirmationToken;
import com.blog_java.domain.models.User;
import com.blog_java.domain.ports.EmailSender;
import com.blog_java.infra.repositories.ConfirmationTokenRepository;
import com.blog_java.infra.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private PasswordEncoder passwordEncoder;

    private final EmailSender emailSender;


    public UserService(EmailSender emailSender, PasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Transactional
    public User createUser(UserRegisterDto userRegisterDto,String baseUrl)
    {
        if(userRepository.findByEmail(userRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());

        User user = new User(userRegisterDto.firstName(), userRegisterDto.lastName(), userRegisterDto.email(),encryptedPassword, UserRole.USER);

        var savedUser = userRepository.save(user);

        //geração de token

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(savedUser);

        confirmationTokenRepository.save(confirmationToken);

        String link = baseUrl + "/users/confirm?token=" + token;
        String subject = "Confirmação de conta";
        String body = "Clique no link para confirmar sua conta: " + link;

        emailSender.sendEmail(user.getEmail(),subject,body);

        return savedUser;

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

        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Transactional
    public User createUserModerator(UserRegisterDto userRegisterDto,String baseUrl)
    {
        if(userRepository.findByEmail(userRegisterDto.email() )!= null)
        {
            throw new IllegalArgumentException("E-mail already exists");
        }

        String encryptedPassword = passwordEncoder.encode(userRegisterDto.password());

        User user = new User(userRegisterDto.firstName(), userRegisterDto.lastName(), userRegisterDto.email(),encryptedPassword, UserRole.MODERATOR);

        var savedUser = userRepository.save(user);

        //geração de token

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(savedUser);

        confirmationTokenRepository.save(confirmationToken);

        String link = baseUrl + "/users/confirm?token=" + token;
        String subject = "Confirmação de conta";
        String body = "Clique no link para confirmar sua conta: " + link;

        emailSender.sendEmail(user.getEmail(),subject,body);

        return savedUser;
    }

    public User confirmUser(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Token inválido!"));

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            throw new IllegalArgumentException("Token expirado!");
        }

        User user = confirmationToken.getUser();

        if(user.enabled)
        {
            throw new IllegalArgumentException("Conta já está verificada!");
        }

        user.setEnabled(true);

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
