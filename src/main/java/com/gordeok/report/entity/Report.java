package com.gordeok.report.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reporterId;

    @Column(nullable = false)
    private Long targetUserId;

    // 연관된 게시글 (선택)
    private Long postId;

    // 신고 제목
    @Column(nullable = false)
    private String reason;

    // 신고 상세 내용 (기존 엔티티에 누락되어 있던 필드)
    @Column(columnDefinition = "TEXT")
    private String content;

    // 증거 이미지 URLs (쉼표 구분)
    @Column(columnDefinition = "TEXT")
    private String evidenceImages;

    @Builder.Default
    private String status = "PENDING";

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }
}
