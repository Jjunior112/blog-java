package com.blog_java.domain.models;

import com.blog_java.domain.dtos.comment.CommentRegisterDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    @Id
    private String id;

    private String postId;

    private String comment;

    public Comment(CommentRegisterDto commentRegisterDto) {
        this.postId = commentRegisterDto.postId();
        this.comment = commentRegisterDto.comment();
    }
}
