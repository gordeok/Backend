package com.gordeok.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String profileImage;

    @Builder.Default
    private Integer trustScore = 0;

    // 사기 신고 이력 유무 (Report 테이블 count > 0 시 true)
    @Builder.Default
    private Boolean hasScamReport = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.trustScore == null) this.trustScore = 0;
        if (this.hasScamReport == null) this.hasScamReport = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImage = profileImageUrl;
    }

    public void markScamReport() {
        this.hasScamReport = true;
    }
}
