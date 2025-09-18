package com.blog_java.application.services;

import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.comment.UpdateCommentDto;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.infra.repositories.CommentRepository;
import com.blog_java.infra.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostService postService, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @Transactional
    public Comment createComment(CommentRegisterDto commentRegisterDto)
    {
        if(commentRegisterDto.postId().equals("") || commentRegisterDto.comment().equals(""))
        {
            throw new IllegalArgumentException();
        }

        Post post = postService.findPostById(commentRegisterDto.postId());

        Comment comment = new Comment(commentRegisterDto);

        post.addComment(comment.getId());

        postRepository.save(post);

        return commentRepository.save(comment);
    }

    public Page<Comment> findAllComments(String postId, Pageable pagination)
    {
        return commentRepository.findAllByPostId(postId,pagination);
    }

    public Comment findCommentById(String id)
    {
        var optionalComment =  commentRepository.findById(id);

        if(optionalComment.isPresent())
        {
            return optionalComment.get();
        }
        throw new NullPointerException();
    }

    @Transactional
    public Comment updateCommentById(String id, UpdateCommentDto updateCommentDto)
    {
        Comment comment =  findCommentById(id);

        comment.setComment(updateCommentDto.comment());

        return comment;
    }

    @Transactional
    public void deleteComment(String id)
    {
        Comment comment = findCommentById(id);

        commentRepository.delete(comment);
    }

}
