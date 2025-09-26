package com.blog_java.application.services;


import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.user.ClientRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.domain.models.User;
import com.blog_java.infra.repositories.CommentRepository;
import com.blog_java.infra.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentService commentService;

    private CommentRegisterDto commentRegisterDto;

    private PostRegisterDto postRegisterDto;

    private Comment comment;

    private Post post;

    private User user;

    @BeforeEach
    void setup()
    {

        user = new User("teste","teste","teste@teste.com","teste", UserRole.CLIENT);

        postRegisterDto = new PostRegisterDto(1L,"teste","teste",null);

        post = new Post(postRegisterDto,user);

        post.setId(1L);

        commentRegisterDto = new CommentRegisterDto(1L,"teste");

        comment = new Comment(commentRegisterDto,post);

        comment.setId(1L);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar o comentário corretamente")
    void createCommentCase1() {
        // arrange

        when(postService.findPostById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenReturn(comment);

        // act

        var result = commentService.createComment(commentRegisterDto);

        // assert

        verify(commentRepository, times(1)).save(any());

        assertEquals(comment.getId(), result.getId());
        assertEquals("teste", result.getComment());
    }

    @Test
    @DisplayName("Não deveria criar o comentário corretamente")
    void createCommentCase2() {
        // arrange

        when(postService.findPostById(any())).thenReturn(post);
        when(commentRepository.save(any())).thenReturn(comment);


        var errorDto = new CommentRegisterDto(1L, "");
        // act

        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(errorDto));

        // assert

        verify(commentRepository, times(0)).save(any());

    }
    @Test
    @DisplayName("Deveria excluir o comentário corretamente")
    void deleteCommentCase1(){
        //arrange

        var id = comment.getId();

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        //act

        commentService.deleteComment(id);

        //assert

        verify(commentRepository,times(1)).delete(any());
    }

    @Test
    @DisplayName("Não deveria excluir o comentário corretamente")
    void deleteCommentCase2(){
        //arrange

        var id = 5L; //Id inexistente

        //act & assert

        assertThrows(NullPointerException.class, () -> commentService.deleteComment(id));
    }

}
