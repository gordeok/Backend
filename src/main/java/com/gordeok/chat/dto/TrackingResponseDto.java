package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingResponseDto {
    private String courierType;    // GS반값택배, CU반값택배
    private String trackingNumber; // 운송장 번호
    private String trackingUrl;    // 택배 조회 URL
}
