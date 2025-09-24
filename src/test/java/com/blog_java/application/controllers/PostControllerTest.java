package com.blog_java.application.controllers;

import com.blog_java.application.services.PostService;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.post.UpdatePostDto;
import com.blog_java.domain.models.Post;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    // beans necessários para o Security Filter
    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserService userService;

    private Post post;

    private PostRegisterDto postRegisterDto;

    @BeforeEach
    void setup()
    {
        postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste","teste",null);

        post = new Post(postRegisterDto);

        post.setId("68cb4e63fcc669726da66047");
    }
    @Test
    @DisplayName("Deve criar o post ao enviar dados corretamente ")
    @WithMockUser
    void createPostCase1() throws Exception {

        //arrange

        when(postService.createPost(any()))
                .thenReturn(post);

        // act

        var result = mvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRegisterDto)));

        //assert

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("68cb4e63fcc669726da66047"));

    }

    @Test
    @DisplayName("Não deve criar o post ao enviar dados incorretos")
    @WithMockUser
    void createPostCase2() throws Exception {

        //arrange

        var errorDto = new PostRegisterDto("","","",null);

        // act

        var result = mvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorDto)));

        //assert

        result.andExpect(status().isBadRequest());

    }
    @Test
    @DisplayName("Deveria atualizar o post corretamente")
    void putPostCase1() throws Exception {
        // arrange
        String id = "68cb4e63fcc669726da66047";

        UpdatePostDto updatePostDto = new UpdatePostDto("teste","",null);

        postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste","",null);

        Post postUpdated = new Post(postRegisterDto);

        postUpdated.setId("68cb4e63fcc669726da66047");

        when(postService.UpdatePostById(eq(id), any(UpdatePostDto.class)))
                .thenReturn(post);

        // act
        var result = mvc.perform(put("/post/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostDto)));

        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.post").value("teste"));

        verify(postService, times(1)).UpdatePostById(eq(id), any(UpdatePostDto.class));
    }

    @Test
    @DisplayName("")
    @WithMockUser
    void deletePostCase1() throws Exception {
        // arrange
        String id = "68cb4e63fcc669726da66047";

        doNothing().when(postService).deletePost(id);

        // act
        var result = mvc.perform(delete("/post/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isNoContent());

        verify(postService, times(1)).deletePost(id);

    }

}
