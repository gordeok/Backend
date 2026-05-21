package com.gordeok.participation.entity;

import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.entity.Post;
import com.gordeok.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 게시글 참여인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 어떤 멤버를 선택했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_item_id")
    private MemberItem memberItem;

    // 참여자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    // 받으시는 분
    private String realName;

    // 전화번호
    private String phoneNumber;

    // 편의점 지점명
    private String storeName;

    // 요청사항
    @Column(columnDefinition = "TEXT")
    private String requestMessage;

    // 택배사 (GS반값택배, CU반값택배)
    private String courierType;

    // 운송장 번호
    private String trackingNumber;

    @Builder.Default
    private String status = "COMPLETED";

    // 운송장 번호 업데이트
    public void updateTracking(String courierType, String trackingNumber) {
        this.courierType = courierType;
        this.trackingNumber = trackingNumber;
    }

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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