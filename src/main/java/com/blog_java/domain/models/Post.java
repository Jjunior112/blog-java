package com.blog_java.domain.models;

import com.blog_java.domain.dtos.post.PostRegisterDto;
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


    public Post(PostRegisterDto postRegisterDto) {
        this.userId = postRegisterDto.userId();
        this.post = postRegisterDto.post();
    }

    public void addComment(Long commentId)
    {
        this.commentIds.add(commentId);
    }
}
