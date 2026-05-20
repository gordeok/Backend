package com.gordeok.post.dto;

import com.gordeok.user.entity.User;
import lombok.Getter;

@Getter
public class SellerInfoDto {

    private Long sellerId;
    private String nickname;
    private String profileImage;
    private Integer trustScore;

    public SellerInfoDto(User user) {

        this.sellerId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.trustScore = user.getTrustScore();
    }
}