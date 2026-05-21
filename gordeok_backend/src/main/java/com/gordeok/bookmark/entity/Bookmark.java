package com.gordeok.bookmark.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    private LocalDateTime createdAt;

    public Bookmark(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
        this.createdAt = LocalDateTime.now();
    }
}
