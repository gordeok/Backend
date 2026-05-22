package com.gordeok.user.dto;

import com.gordeok.user.entity.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MyProfileResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private Integer trustScore;
    private Boolean hasScamReport;
    private LocalDateTime createdAt;

    public MyProfileResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.trustScore = user.getTrustScore();
        this.hasScamReport = user.getHasScamReport();
        this.createdAt = user.getCreatedAt();
    }
}
