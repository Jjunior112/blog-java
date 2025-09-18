package com.blog_java.infra.repositories;

import com.blog_java.domain.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
