package com.blog_java.application.services;

import com.blog_java.domain.dtos.post.PostRegisterDto;
import com.blog_java.domain.dtos.post.UpdatePostDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Post;
import com.blog_java.domain.models.User;
import com.blog_java.infra.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public Post createPost(PostRegisterDto postRegisterDto,byte[] image){

        User user = userService.findById(postRegisterDto.userId());

        if(user == null || postRegisterDto.post() == "" )
        {
            throw new IllegalArgumentException();
        }

        Post post = new Post(postRegisterDto,user);

        if(image!=null)
        {

            post.setImage(image);
        }

        return postRepository.save(post);
    }

    public Page<Post> findAllPosts(Long userId, Pageable pagination)
    {
        return postRepository.findAllByUserIdAndIsVerifiedTrue(userId,pagination);
    }

    public Page<Post> findAllPostsUnverified(Long userId, Pageable pagination)
    {
        return postRepository.findAllByUserIdAndIsVerifiedFalse(userId,pagination);
    }

    public Post findPostById(Long id)
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
    public Post updatePostById(Long id, UpdatePostDto updatePostDto, byte[] image) {
        Post post = findPostById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isOwner = post.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals(UserRole.ADMIN);
        boolean isModerator = user.getRole().equals(UserRole.MODERATOR);

        if (!isOwner && !isAdmin && !isModerator) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este post.");
        }

        if (updatePostDto.title() != null) {
            post.setTitle(updatePostDto.title());
        }

        if (updatePostDto.post() != null) {
            post.setPost(updatePostDto.post());
        }

        if (updatePostDto.imageBase64() != null) {
            post.setImage(updatePostDto.imageBase64());
        }

        if (image != null) {
            post.setImage(image);
        }

        return postRepository.save(post);
    }

    @Transactional
    public Post verifyPostById(Long id)
    {
        Post post = findPostById(id);

        if (post.getIsVerified()) {
            throw new IllegalStateException("O post já está verificado.");
        }

        post.setIsVerified(true);

        return postRepository.save(post);

    }

    @Transactional
    public void deletePost(Long id)
    {
        Post post = findPostById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isOwner = post.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals( UserRole.ADMIN);
        boolean isModerator = user.getRole().equals( UserRole.MODERATOR);

        if (!isOwner && !isAdmin && !isModerator) {
            throw new AccessDeniedException("Você não tem permissão para excluir este post.");
        }

        postRepository.delete(post);
    }
}
