package com.gordeok.post.entity;

import com.gordeok.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    private String idolName;

    private String albumName;

    @ElementCollection
    @CollectionTable(name = "post_components", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "component")
    private List<String> components;  // 구성품 목록

    private String shippingFeeType;

    @Builder.Default
    private String status = "OPEN";

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Integer scrapCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 거래 완료
    public void complete() {
        this.status = "COMPLETED";
    }

    // 거래 취소 (글 내림)
    public void close() {
        this.status = "CLOSED";
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
