package com.blog_java.application.controllers;

import com.blog_java.application.services.CommentService;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    private CommentRegisterDto commentRegisterDto;

    private PostRegisterDto postRegisterDto;

    private Comment comment;

    private Post post;

    private User user;

    @BeforeEach
    void setup()
    {

        user = new User("teste","teste","teste@teste.com","teste", UserRole.USER);

        postRegisterDto = new PostRegisterDto(1L,"teste","teste",null);

        post = new Post(postRegisterDto,user);

        post.setId(1L);

        commentRegisterDto = new CommentRegisterDto(1L,"teste");

        comment = new Comment(commentRegisterDto,post);

        comment.setId(1L);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deveria retornar 200 e registrar comentário")
    @WithMockUser
    void createCommentCase1() throws Exception {
        //arrange

        when(commentService.createComment(any())).thenReturn(comment);

        //act

        var result = mvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRegisterDto)));
        //assert

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deveria retornar 400 e não registrar comentário")
    @WithMockUser
    void createCommentCase2() throws Exception {
        //arrange

        var errorDto = new CommentRegisterDto(1L,null);

        //act

        var result = mvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorDto)));
        //assert

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria retonar 204 e deletar comentário")
    @WithMockUser

    void deleteCommentCase1() throws Exception {
        // arrange
        Long id = 1L;

        doNothing().when(commentService).deleteComment(id);

        // act
        var result = mvc.perform(delete("/comment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(id);
    }

}
