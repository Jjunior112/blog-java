package com.blog_java.infra.repositories;

import com.blog_java.domain.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
}
