package com.gordeok.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TrackingShareRequestDto {
    private List<TrackingShareItemDto> trackingList; // 구매자별 운송장 정보 목록
}
