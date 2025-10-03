package com.blog_java.application.controllers.v1;

import com.blog_java.application.services.PostService;
import com.blog_java.application.services.StatusService;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.domain.models.User;
import com.blog_java.domain.ports.EmailSender;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatusController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @MockBean

    private EmailSender emailSender;

    // beans necessários para o Security Filter
    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    @MockBean
    private StatusService statusService;

    private CommentRegisterDto commentRegisterDto;

    private PostRegisterDto postRegisterDto;

    private Comment comment;

    private Post post;

    private User user;

    @BeforeEach
    void setup()
    {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Deve retornar status detalhado do banco de dados")
    @WithMockUser
    void getStatusCase1() throws Exception {
        // arrange
        Map<String, Object> databaseStatus = Map.of(
                "version", "15.14 (Debian 15.14-1.pgdg13+1)",
                "max_conn", 100,
                "used_conn", 10
        );

        when(statusService.getDatabaseStatus()).thenReturn(databaseStatus);

        // act
        var result = mvc.perform(get("/api/v1/status")
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.updated_at").exists())
                .andExpect(jsonPath("$.dependencies.database.version").value("15.14 (Debian 15.14-1.pgdg13+1)"))
                .andExpect(jsonPath("$.dependencies.database.max_conn").value(100))
                .andExpect(jsonPath("$.dependencies.database.used_conn").value(10));

        verify(statusService, times(1)).getDatabaseStatus();
    }
}