package com.blog_java.infra.repositories;

import com.blog_java.domain.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, Long> {
}
