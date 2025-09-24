package com.blog_java.domain.dtos.post;

import com.blog_java.domain.models.Post;

public record PostListDto(String id, String userId, String title, String post, byte[] image) {
    public PostListDto(Post post) { this(post.getId(), post.getUserId(), post.getTitle(), post.getPost(), post.getImage());
    }
}
