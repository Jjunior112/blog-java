package com.blog_java.application.services;

import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.comment.UpdateCommentDto;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.infra.repositories.CommentRepository;
import com.blog_java.infra.repositories.PostRepository;
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
        Post post =postService.findPostById(commentRegisterDto.postId());

        Comment comment = new Comment(commentRegisterDto);

        post.addComment(comment.getId());

        postRepository.save(post);

        return commentRepository.save(comment);
    }

    public List<Comment> findAllComments(Long postId)
    {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment findCommentById(Long id)
    {
        var optionalComment =  commentRepository.findById(id);

        if(optionalComment.isPresent())
        {
            return optionalComment.get();
        }
        throw new NullPointerException();
    }

    @Transactional
    public Comment updateCommentById(Long id, UpdateCommentDto updateCommentDto)
    {
        Comment comment =  findCommentById(id);

        comment.setComment(updateCommentDto.comment());

        return comment;
    }

    @Transactional
    public void deleteComment(Long id)
    {
        Comment comment = findCommentById(id);

        commentRepository.delete(comment);
    }

}
