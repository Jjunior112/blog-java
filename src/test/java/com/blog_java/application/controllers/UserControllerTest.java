package com.blog_java.application.controllers;

import com.blog_java.application.controllers.v1.UserController;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.user.*;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private User user;

    @BeforeEach
    void setup() {
        user = new User("Teste", "User", "teste@teste.com", "123456", UserRole.USER);
        user.setId(1L);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve realizar login corretamente")
    void loginCase1() throws Exception {
        // arrange
        LoginDto loginDto = new LoginDto("teste@teste.com", "123456");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        when(tokenService.generateToken(any(User.class)))
                .thenReturn("fake-jwt-token");

        // act
        var result = mvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenService, times(1)).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Deve registrar usuário comum corretamente")
    void registerUserCase1() throws Exception {
        // arrange
        UserRegisterDto registerDto = new UserRegisterDto("Teste", "User", "teste@teste.com", "123456");
        when(userService.createUser(any(UserRegisterDto.class), any()))
                .thenReturn(user);

        // act
        var result = mvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));

        verify(userService, times(1)).createUser(any(UserRegisterDto.class), any());
    }

    @Test
    @DisplayName("Deve confirmar usuário com token válido")
    void confirmUserCase1() throws Exception {
        // arrange
        String token = "fake-token";
        when(userService.confirmUser(eq(token))).thenReturn(user);

        // act
        var result = mvc.perform(get("/api/v1/users/confirm")
                .param("token", token));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bem vindo," + user.getFirstName() + "! Conta confirmada com sucesso!"));

        verify(userService, times(1)).confirmUser(token);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void getUserByIdCase1() throws Exception {
        // arrange
        when(userService.findById(1L)).thenReturn(user);

        // act
        var result = mvc.perform(get("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));

        verify(userService, times(1)).findById(1L);
    }
}
