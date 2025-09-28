package com.blog_java.domain.models;

import com.blog_java.domain.dtos.post.PostRegisterDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // cria a FK em posts
    private User user;

    @Lob
    private byte[] image = null;

    private String title;

    private String post;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private Boolean isVerified = false;

    public Post(PostRegisterDto postRegisterDto, User user) {
        this.user = user;
        this.title = postRegisterDto.title();
        this.post = postRegisterDto.post();
        if(postRegisterDto.imageBase64()!=null)
        {
            this.image = postRegisterDto.imageBase64();
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

}
