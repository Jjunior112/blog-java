package com.blog_java.application.services;

import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.comment.UpdateCommentDto;
import com.blog_java.domain.enums.UserRole;
import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;
import com.blog_java.domain.models.User;
import com.blog_java.infra.repositories.CommentRepository;
import com.blog_java.infra.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment comment = new Comment(commentRegisterDto,post,user);

        post.addComment(comment);

        return commentRepository.save(comment);
    }

    public Page<Comment> findAllComments(Long postId, Pageable pagination)
    {
        return commentRepository.findAllByPostId(postId,pagination);
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
    public Comment updateCommentById(Long id, UpdateCommentDto updateCommentDto) {
        Comment comment = findCommentById(id);

        // Usuário autenticado
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isOwner = comment.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals(UserRole.ADMIN);
        boolean isModerator = user.getRole().equals(UserRole.MODERATOR);


        if (!isOwner && !isAdmin && !isModerator) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este comentário.");
        }

        comment.setComment(updateCommentDto.comment());

        return commentRepository.save(comment);
    }
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = findCommentById(id);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Permissões em um set
        Set<UserRole> roles = Set.of(UserRole.ADMIN, UserRole.MODERATOR);

        if (!comment.getUser().getId().equals(user.getId()) &&
                !comment.getPost().getUser().getId().equals(user.getId()) &&
                roles.stream().noneMatch(role -> role.equals(user.getRole()))) {
            throw new AccessDeniedException("Você não tem permissão para excluir este comentário.");
        }

        commentRepository.delete(comment);
    }

}
