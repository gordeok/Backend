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
public class ChatMenuResponseDto {
    private Long chatRoomId;
    private String title;          // 아이돌명 + 앨범명
    private String postStatus;     // "모집중" or "모집완료" (member_items 기반 실시간 계산)
    private String myRole;         // SELLER or BUYER
    private List<ChatMenuParticipantDto> participants; // 대화상대 목록 (본인 제외)
}
