package com.blog_java.domain.models;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Post {
    @Id
    private Long id;

    private String userId;

    private String post;

    private List<Long> commentIds = new ArrayList<>();

    public Post(String userId,String post) {
        this.userId = userId;
        this.post = post;
    }

    public void addComment(Long commentId)
    {
        this.commentIds.add(commentId);
    }
}
