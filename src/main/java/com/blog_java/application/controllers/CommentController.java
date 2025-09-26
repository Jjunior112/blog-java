package com.blog_java.application.controllers;

import com.blog_java.application.services.CommentService;
import com.blog_java.domain.dtos.comment.CommentListDto;
import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import com.blog_java.domain.dtos.comment.UpdateCommentDto;
import com.blog_java.domain.models.Comment;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@SecurityRequirement(name = "bearer-key")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentListDto> createComment(@RequestBody @Valid CommentRegisterDto commentRegisterDto)
    {
        var response = commentService.createComment(commentRegisterDto);

        return ResponseEntity.ok(new CommentListDto(response));
    }

    @GetMapping
    public ResponseEntity<Page<Comment>> getAllComments(@RequestParam Long postId, @PageableDefault(size = 10,sort = {"comment"}) Pageable pagination)
    {
        var comments = commentService.findAllComments(postId,pagination);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentListDto> getCommentById(@PathVariable Long id)
    {
        var comment = commentService.findCommentById(id);

        return ResponseEntity.ok(new CommentListDto(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentListDto> updateComment(@PathVariable Long id, @RequestBody @Valid UpdateCommentDto updateCommentDto)
    {
        var comment = commentService.updateCommentById(id,updateCommentDto);

        return ResponseEntity.ok(new CommentListDto(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id)
    {
        commentService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }

}
