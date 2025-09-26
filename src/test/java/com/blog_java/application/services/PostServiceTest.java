package com.blog_java.application.services;


import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.domain.models.User;
import com.blog_java.infra.repositories.PostRepository;
import com.blog_java.infra.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    private CommentRegisterDto commentRegisterDto;

    private PostRegisterDto postRegisterDto;

    private Comment comment;

    private Post post;

    private User user;

    @BeforeEach
    void setup()
    {
        user = new User("teste","teste","teste@teste.com","teste", UserRole.CLIENT);

        user.setId(1L);

        postRegisterDto = new PostRegisterDto(1L,"teste","teste",null);

        post = new Post(postRegisterDto,user);

        post.setId(1L);

        commentRegisterDto = new CommentRegisterDto(1L,"teste");

        comment = new Comment(commentRegisterDto,post);

        comment.setId(1L);

        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Deveria criar o post corretamente")
    void createPostCase1()
    {
        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto(1L,"teste","teste",null);

        when(userService.findById(any())).thenReturn(user);

        when(postRepository.save(any())).thenReturn(post);

        //act

        var result = postService.createPost(postRegisterDto,null);

        //assert

        verify(postRepository, times(1)).save(any());

        assertEquals(postRegisterDto.post(),result.getPost(),"the post should be equal");

    }
    @Test
    @DisplayName("Não deveria criar o post corretamente")
    void createPostCase2()
    {

        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto(1L, "","",null);

        when(userService.findById(any())).thenReturn(user);

        // act

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(postRegisterDto,null));

        //assert

        verify(postRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("Não deveria criar o post corretamente ao enviar usuario nulo")
    void createPostCase3()
    {

        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto(null, "","",null);


        // act

        assertThrows(NullPointerException.class, () -> postService.createPost(postRegisterDto,null));

        //assert

        verify(postRepository, times(0)).save(any());

    }


    @Test
    @DisplayName("Deveria excluir o post corretamente")
    void deletePostCase1(){
        //arrange

        var id = post.getId();

        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        //act

        postService.deletePost(id);

        //assert

        verify(postRepository,times(1)).delete(any());
    }

    @Test
    @DisplayName("Não deveria excluir o post corretamente")
    void deletePostCase2(){
        //arrange

        var id = 5L; //Id inexistente

        //act & assert

        assertThrows(NullPointerException.class, () -> postService.deletePost(id));
    }
}
