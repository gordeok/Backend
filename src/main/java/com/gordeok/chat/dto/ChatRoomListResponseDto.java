package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomListResponseDto {
    private Long chatRoomId;
    private String title;           // 아이돌명 + 앨범명
    private String sellerName;      // 판매자 닉네임
    private int currentMembers;     // 현재 참여 인원
    private int maxMembers;         // 정원
    private String lastMessage;     // 마지막 메시지
    private int unreadCount;        // 읽지 않은 메시지 수
    private LocalDateTime lastMessageTime; // 마지막 메시지 시간
    private String status;          // progress, done (프론트 표기 기준)
    private String myRole;          // SELLER, BUYER
    private String type;            // GROUP(분철 단톡방), DIRECT(쪽지)
}
