package com.gordeok.user.dto;

import com.gordeok.review.entity.Review;
import com.gordeok.user.entity.User;
import lombok.Getter;
import java.time.LocalDateTime;

// 판매 완료 후기 보기 / 받은 후기 목록 공통 DTO
@Getter
public class MyPostReviewResponseDto {

    private Long reviewId;
    private Long reviewerId;
    private String reviewerNickname;
    private String reviewerProfileImage;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;

    public MyPostReviewResponseDto(Review review, User reviewer) {
        this.reviewId = review.getId();
        this.reviewerId = review.getReviewerId();
        this.reviewerNickname = reviewer != null ? reviewer.getNickname() : "알 수 없음";
        this.reviewerProfileImage = reviewer != null ? reviewer.getProfileImage() : null;
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
