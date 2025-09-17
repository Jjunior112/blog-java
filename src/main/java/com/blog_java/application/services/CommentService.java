package com.blog_java.application.services;

import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.comment.UpdateCommentDto;
import com.blog_java.domain.models.Comment;
import com.blog_java.infra.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment createComment(CommentRegisterDto commentRegisterDto)
    {
        Comment comment = new Comment(commentRegisterDto);

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
