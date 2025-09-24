package com.blog_java.application.services;


import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.models.Post;
import com.blog_java.infra.repositories.PostRepository;
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

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setup()
    {
        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste","teste",null);

        post = new Post(postRegisterDto);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deveria criar o post corretamente")
    void createPostCase1()
    {
        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste","teste",null);

        when(postRepository.save(any())).thenReturn(post);

        //act

        var result = postService.createPost(postRegisterDto);

        //assert

        verify(postRepository, times(1)).save(any());

        assertEquals(postRegisterDto.post(),result.getPost(),"the post should be equal");

    }
    @Test
    @DisplayName("Não deveria criar o post corretamente")
    void createPostCase2()
    {

        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789", "","",null);

        // act

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(postRegisterDto));

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

        var id = "68cb4e63fcc669726da66045"; //Id inexistente

        //act & assert

        assertThrows(NullPointerException.class, () -> postService.deletePost(id));
    }
}
