package com.blog_java.domain.dtos.comment;

import com.blog_java.domain.models.Comment;
import com.blog_java.domain.models.Post;

public record CommentListDto(Long id, Long postId, String comment ) {

    public CommentListDto(Comment comment) {this(comment.getId(),comment.getPost().getId(),comment.getComment());}

}
