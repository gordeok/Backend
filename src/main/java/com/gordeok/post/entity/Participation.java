package com.gordeok.post.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberItemId; // 어떤 멤버 슬롯인지

    @Column(nullable = false)
    private Long buyerId; // 구매자 userId

    private String recipientName;   // 받으시는 분
    private String phoneNumber;     // 전화번호
    private String convenienceStore; // 편의점 지점명
    private String request;          // 요청사항

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
