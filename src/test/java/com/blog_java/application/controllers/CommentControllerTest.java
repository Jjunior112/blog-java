package com.blog_java.application.controllers;

import com.blog_java.application.services.CommentService;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.models.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    private Comment comment;

    private CommentRegisterDto commentRegisterDto;

    @BeforeEach
    void setup()
    {
        commentRegisterDto = new CommentRegisterDto("66f3e9a4c0b12345abcd6789","teste");

        comment = new Comment(commentRegisterDto);

        comment.setId("68cb4e63fcc669726da66047");
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
                .andExpect(jsonPath("$.id").value("68cb4e63fcc669726da66047"));
    }

    @Test
    @DisplayName("Deveria retornar 400 e não registrar comentário")
    @WithMockUser
    void createCommentCase2() throws Exception {
        //arrange

        var errorDto = new CommentRegisterDto("",null);

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
        String id = "68cb4e63fcc669726da66047";

        doNothing().when(commentService).deleteComment(id);

        // act
        var result = mvc.perform(delete("/comment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(id);
    }

}
