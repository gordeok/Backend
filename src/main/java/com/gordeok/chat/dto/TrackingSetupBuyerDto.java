package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingSetupBuyerDto {
    private Long buyerUserId;   // 구매자 userId
    private String nickname;    // 구매자 닉네임
    private String memberName;  // 선택한 멤버명
}
