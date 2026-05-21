package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMenuParticipantDto {
    private Long userId;
    private String nickname;
    private String memberName; // 선택한 멤버 이름
    private String role;       // SELLER or BUYER
}
