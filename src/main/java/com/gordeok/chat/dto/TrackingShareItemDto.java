package com.gordeok.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TrackingShareItemDto {
    private Long buyerUserId;      // 구매자 userId
    private String courierType;    // GS반값택배, CU반값택배
    private String trackingNumber; // 운송장 번호
}
