package com.gordeok.review.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewerId;   // 후기 작성자 (구매자)

    @Column(nullable = false)
    private Long targetUserId; // 후기 대상 (판매자)

    @Column(nullable = false)
    private Long chatRoomId;   // 어느 거래인지

    @Column(nullable = false)
    private Integer rating;    // 별점 (1~5)

    @Column(columnDefinition = "TEXT")
    private String content;    // 후기 내용

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
