package com.blog_java.domain.dtos.post;

import com.blog_java.domain.models.Post;

public record PostListDto(String id, String userId, String post) {
    public PostListDto(Post post) { this(post.getId(), post.getUserId(),post.getPost());
    }
}
