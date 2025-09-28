package com.blog_java.domain.dtos.post;

import com.blog_java.domain.models.Post;

import java.util.List;

public record PostListDto(Long id, Long userId, String title, String post, byte[] image, List comments, Boolean isVerified) {
    public PostListDto(Post post) { this(post.getId(), post.getUser().getId(), post.getTitle(), post.getPost(), post.getImage(), post.getComments(),post.getIsVerified());
    }
}
