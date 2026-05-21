package com.gordeok.chat.controller;

import com.gordeok.chat.dto.ChatMessageRequestDto;
import com.gordeok.chat.dto.ChatRoomListResponseDto;
import com.gordeok.chat.dto.MessageResponseDto;
import com.gordeok.chat.service.ChatMessageService;
import com.gordeok.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    /**
     * 내 채팅방 목록 조회 (분철 단톡방 + 쪽지)
     * GET /api/chat-rooms?userId=1
     */
    @GetMapping("/api/chat-rooms")
    public ResponseEntity<List<ChatRoomListResponseDto>> getMyChatRooms(
            @RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

    /**
     * 특정 채팅방의 메시지 목록 조회
     * GET /api/chat-rooms/{chatRoomId}/messages
     */
    @GetMapping("/api/chat-rooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageResponseDto>> getMessages(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getMessages(chatRoomId));
    }

    /**
     * WebSocket 메시지 전송
     * 클라이언트 publish: /pub/chat/message
     * 서버 broadcast: /sub/chat/rooms/{chatRoomId}
     * Body: { chatRoomId, senderId, content, messageType }
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequestDto dto) {
        chatMessageService.sendMessage(dto);
    }
}
