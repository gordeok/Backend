package com.gordeok.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewResponseDto {
    private Long reviewId;
    private String message;
}
