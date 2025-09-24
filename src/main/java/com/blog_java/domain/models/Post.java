package com.blog_java.domain.models;

import com.blog_java.domain.dtos.post.PostRegisterDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    private String id;

    private String userId;

    private byte[] image = null;

    private String title;

    private String post;

    private List<String> commentIds = new ArrayList<>();

    public Post(PostRegisterDto postRegisterDto) {
        this.userId = postRegisterDto.userId();
        this.title = postRegisterDto.title();
        this.post = postRegisterDto.post();
        if(postRegisterDto.image()!=null)
        {
            this.image = postRegisterDto.image();
        }
    }

    public void addComment(String commentId)
    {
        this.commentIds.add(commentId);
    }
}
