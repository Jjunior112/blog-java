package com.blog_java.domain.dtos.comment;

import com.blog_java.domain.models.Comment;

public record CommentListDto(String id, String postId, String comment ) {

    public CommentListDto(Comment comment) {this(comment.getId(),comment.getPostId(),comment.getComment());}

}
