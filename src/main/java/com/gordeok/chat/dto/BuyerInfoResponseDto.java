package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerInfoResponseDto {
    private String nickname;       // 구매자 닉네임
    private String memberName;     // 선택한 멤버 이름
    private String realName;       // 받으시는 분
    private String phoneNumber;    // 전화번호
    private String storeName;      // 편의점 지점명
    private String requestMessage; // 요청사항
}
