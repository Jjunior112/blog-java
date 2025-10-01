package com.blog_java.application.controllers;

import com.blog_java.application.services.PostService;
import com.blog_java.application.services.TokenService;
import com.blog_java.application.services.UserService;
import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.post.UpdatePostDto;
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

    @MockBean

    private EmailSender emailSender;

    // beans necessários para o Security Filter
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

        comment = new Comment(commentRegisterDto,post,user);

        comment.setId(1L);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar o post ao enviar dados corretamente ")
    @WithMockUser
    void createPostCase1() throws Exception {

        //arrange

        when(postService.createPost(any(),any()))
                .thenReturn(post);

        // act
        var result = mvc.perform(multipart("/post")
                .param("userId","1")
                .param("title", postRegisterDto.title())
                .param("post", postRegisterDto.post())
        );

        //assert

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    @DisplayName("Não deve criar o post ao enviar dados incorretos")
    @WithMockUser
    void createPostCase2() throws Exception {

        //arrange

        var errorDto = new PostRegisterDto(1L,"","",null);

        // act

        var result = mvc.perform(multipart("/post")
                .param("userId","")
                .param("title", errorDto.title())
                .param("post", errorDto.post())
        );

        //assert

        result.andExpect(status().isBadRequest());

    }
    @Test
    @DisplayName("Deveria atualizar o post corretamente")
    void putPostCase1() throws Exception {
        // arrange
        Long id = 1L;

        UpdatePostDto updatePostDto = new UpdatePostDto("teste","",null);

        postRegisterDto = new PostRegisterDto(1L,"teste","",null);

        Post postUpdated = new Post(postRegisterDto,user);

        postUpdated.setId(1L);

        when(postService.updatePostById(eq(id), any(UpdatePostDto.class),any()))
                .thenReturn(post);

        // act
        var result = mvc.perform(multipart("/post/{id}",id)
                .param("title", updatePostDto.title())
                .param("post", updatePostDto.post())
                .with(request -> { request.setMethod("PUT"); return request; }) // forçar PUT
        );
        // assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.post").value("teste"));

        verify(postService, times(1)).updatePostById(eq(id), any(UpdatePostDto.class),any());
    }

    @Test
    @DisplayName("")
    @WithMockUser
    void deletePostCase1() throws Exception {
        // arrange
        Long id = 1L;

        doNothing().when(postService).deletePost(id);

        // act
        var result = mvc.perform(delete("/post/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isNoContent());

        verify(postService, times(1)).deletePost(id);

    }

}
