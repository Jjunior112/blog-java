package com.blog_java.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "confirmation_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;


}