package com.blog_java.infra.repositories;

import com.blog_java.domain.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByUserId(Long userId, Pageable pagination);
}
