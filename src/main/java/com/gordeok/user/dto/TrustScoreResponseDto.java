package com.gordeok.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TrustScoreResponseDto {
    private Integer totalScore;
    private Breakdown breakdown;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Breakdown {
        private Double transactionCompleteRate;
        private Integer chatResponseSpeed;
        private Integer reportCount;
    }
}
