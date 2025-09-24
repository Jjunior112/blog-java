package com.blog_java.application.services;

import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.post.UpdatePostDto;
import com.blog_java.domain.models.Post;
import com.blog_java.infra.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Post createPost(PostRegisterDto postRegisterDto){

        if(postRegisterDto.userId().equals("") || postRegisterDto.post().equals("") )
        {
            throw new IllegalArgumentException();
        }

        Post post = new Post(postRegisterDto);

        return postRepository.save(post);
    }

    public Page<Post> findAllPosts(String userId,Pageable pagination)
    {
        return postRepository.findAllByUserId(userId,pagination);
    }

    public Post findPostById(String id)
    {
        var optionalPost = postRepository.findById(id);

        if(optionalPost.isPresent())
        {
            Post post = optionalPost.get();

            return post;
        }

        throw new NullPointerException();
    }

    @Transactional
    public Post UpdatePostById(String id, UpdatePostDto updatePostDto)
    {
        Post post = findPostById(id);

        if(updatePostDto.title()!=null)
        {
            post.setTitle(updatePostDto.title());
        }

        if(updatePostDto.post()!=null)
        {
            post.setPost(updatePostDto.post());
        }

        if(updatePostDto.image()!=null)
        {
            post.setImage(updatePostDto.image());
        }

        return postRepository.save(post);
    }

    public void deletePost(String id)
    {
        Post post = findPostById(id);

        postRepository.delete(post);
    }
}
