package com.gordeok.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private String messageType; // TEXT, IMAGE
}
