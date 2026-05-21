package com.gordeok.chat.dto;

import com.gordeok.chat.entity.Message;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;

    public MessageResponseDto(Message message, String senderNickname) {
        this.messageId = message.getId();
        this.senderId = message.getSenderId();
        this.senderNickname = senderNickname;
        this.content = message.getContent();
        this.messageType = message.getMessageType();
        this.createdAt = message.getCreatedAt();
    }

    // 시스템 메시지 (거래 완료 알림 등)
    public static MessageResponseDto systemMessage(Long chatRoomId, String content, String messageType) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.senderId = null;
        dto.senderNickname = "SYSTEM";
        dto.content = content;
        dto.messageType = messageType;
        dto.createdAt = LocalDateTime.now();
        return dto;
    }

    private MessageResponseDto() {}
}
