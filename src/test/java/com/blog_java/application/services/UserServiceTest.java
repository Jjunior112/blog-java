package com.blog_java.application.services;

import com.blog_java.domain.dtos.user.UserRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.ConfirmationToken;
import com.blog_java.domain.models.User;
import com.blog_java.domain.ports.EmailSender;
import com.blog_java.infra.repositories.ConfirmationTokenRepository;
import com.blog_java.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deveria criar o usuário corretamente e enviar e-mail de confirmação de conta")
    void createUserCase1() {
        // Arrange
        UserRegisterDto dto = new UserRegisterDto("Teste", "teste", "teste@teste.com", "123456");


        when(passwordEncoder.encode(dto.password())).thenReturn("encoded-password");

        User savedUser = new User(dto.firstName(), dto.lastName(), dto.email(), "encoded-password", UserRole.USER);
        savedUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.createUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.email(), result.getEmail());
        verify(userRepository).findByEmail(dto.email());
        verify(userRepository).save(any(User.class));
        verify(confirmationTokenRepository).save(any(ConfirmationToken.class));
        verify(emailSender).sendEmail(eq(dto.email()), anyString(), contains("/users/confirm?token="));
    }

    @Test
    @DisplayName("Não deveria criar o usuário com e-mail duplicado")
    void createUserCase2() {
        // Arrange
        UserRegisterDto dto = new UserRegisterDto("Teste", "teste", "teste@teste.com", "123456");

        // Simula que o usuário já existe no banco
        User existingUser = new User(dto.firstName(), dto.lastName(), dto.email(), "encoded-password", UserRole.USER);
        existingUser.setId(99L);

        when(userRepository.findByEmail(dto.email())).thenReturn(existingUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(dto)
        );

        assertEquals("E-mail already exists", exception.getMessage());

        // Verifica que NÃO chegou a salvar nem enviar e-mail
        verify(userRepository, never()).save(any(User.class));
        verify(confirmationTokenRepository, never()).save(any(ConfirmationToken.class));
        verify(emailSender, never()).sendEmail(anyString(), anyString(), anyString());
    }
    @Test
    @DisplayName("Deveria confirmar a conta do usuário corretamente")
    void confirmUserCase1() {
        // Arrange
    User user = new User("Teste", "teste", "teste@teste.com", "encoded", UserRole.USER);
        user.setId(1L);
        user.setEnabled(false);

        ConfirmationToken token = new ConfirmationToken();
        token.setToken("valid-token");
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(confirmationTokenRepository.findByToken("valid-token"))
                .thenReturn(Optional.of(token));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User confirmedUser = userService.confirmUser("valid-token");

        // Assert
        assertTrue(confirmedUser.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Não deveria confirmar a conta do usuário com token expirado")
    void confirmUserCase2() {
        // Arrange
        User user = new User("Teste", "Teste", "teste@teste.com", "encoded", UserRole.USER);
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("expired-token");
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(confirmationTokenRepository.findByToken("expired-token"))
                .thenReturn(Optional.of(token));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.confirmUser("expired-token"),
                "Token expirado!");
    }
}
