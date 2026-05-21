package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingSetupResponseDto {
    private String defaultCourierType;          // 분철글에 등록된 배송 방법 (post.shippingFeeType)
    private List<TrackingSetupBuyerDto> buyers; // 구매자 목록 (닉네임 + 선택 멤버명)
}
