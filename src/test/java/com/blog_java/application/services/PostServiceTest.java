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
import org.springframework.beans.factory.annotation.Autowired;

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
        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste");

        post = new Post(postRegisterDto);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deveria criar o post corretamente")
    void createPostCase1()
    {
        //arrange

        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789","teste");

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

        PostRegisterDto postRegisterDto = new PostRegisterDto("66f3e9a4c0b12345abcd6789", "");

        // act

        assertThrows(IllegalArgumentException.class, () -> postService.createPost(postRegisterDto));

        //assert

        verify(postRepository, times(0)).save(any());

    }
}
