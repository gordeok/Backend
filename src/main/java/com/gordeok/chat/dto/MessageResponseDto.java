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
}
