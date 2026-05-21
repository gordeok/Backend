package com.gordeok.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {
    private Long reviewerId;   // 후기 작성자 (구매자)
    private Long targetUserId; // 후기 대상 (판매자)
    private Long chatRoomId;   // 어느 거래인지
    private Integer rating;    // 별점 (1~5)
    private String content;    // 후기 내용
}
