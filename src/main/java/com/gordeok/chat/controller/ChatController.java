package com.gordeok.chat.controller;

import com.gordeok.chat.dto.*;
import com.gordeok.chat.dto.TrackingSetupResponseDto;
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

    // 내 채팅방 목록 조회
    // GET /api/chat-rooms?userId=1
    @GetMapping("/api/chat-rooms")
    public ResponseEntity<List<ChatRoomListResponseDto>> getMyChatRooms(
            @RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getMyChatRooms(userId));
    }

    // 특정 채팅방의 메시지 목록 조회
    // GET /api/chat-rooms/{chatRoomId}/messages
    @GetMapping("/api/chat-rooms/{chatRoomId}/messages")
    public ResponseEntity<List<MessageResponseDto>> getMessages(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getMessages(chatRoomId));
    }

    // 채팅 메뉴 조회
    // GET /api/chat-rooms/{chatRoomId}/menu?userId=1
    @GetMapping("/api/chat-rooms/{chatRoomId}/menu")
    public ResponseEntity<ChatMenuResponseDto> getChatMenu(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getChatMenu(chatRoomId, userId));
    }

    // 구매자 정보 확인 (판매자 전용)
    // GET /api/chat-rooms/{chatRoomId}/participants/{buyerUserId}/info
    @GetMapping("/api/chat-rooms/{chatRoomId}/participants/{buyerUserId}/info")
    public ResponseEntity<BuyerInfoResponseDto> getBuyerInfo(
            @PathVariable Long chatRoomId,
            @PathVariable Long buyerUserId) {
        return ResponseEntity.ok(chatRoomService.getBuyerInfo(chatRoomId, buyerUserId));
    }

    // 거래 완료 (판매자 전용)
    // PATCH /api/chat-rooms/{chatRoomId}/complete
    @PatchMapping("/api/chat-rooms/{chatRoomId}/complete")
    public ResponseEntity<Void> completeTransaction(@PathVariable Long chatRoomId) {
        chatRoomService.completeTransaction(chatRoomId);
        return ResponseEntity.ok().build();
    }

    // 거래 취소 (판매자 전용)
    // PATCH /api/chat-rooms/{chatRoomId}/cancel
    @PatchMapping("/api/chat-rooms/{chatRoomId}/cancel")
    public ResponseEntity<Void> cancelTransaction(@PathVariable Long chatRoomId) {
        chatRoomService.cancelTransaction(chatRoomId);
        return ResponseEntity.ok().build();
    }

    // 운송장 공유 페이지 초기 데이터 조회 (판매자 전용)
    // GET /api/chat-rooms/{chatRoomId}/tracking/setup
    @GetMapping("/api/chat-rooms/{chatRoomId}/tracking/setup")
    public ResponseEntity<TrackingSetupResponseDto> getTrackingSetup(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatRoomService.getTrackingSetup(chatRoomId));
    }

    // 운송장 번호 공유 (판매자 전용)
    // POST /api/chat-rooms/{chatRoomId}/tracking
    @PostMapping("/api/chat-rooms/{chatRoomId}/tracking")
    public ResponseEntity<Void> shareTracking(
            @PathVariable Long chatRoomId,
            @RequestBody TrackingShareRequestDto dto) {
        chatRoomService.shareTracking(chatRoomId, dto);
        return ResponseEntity.ok().build();
    }

    // 배송 현황 조회 (구매자 전용)
    // GET /api/chat-rooms/{chatRoomId}/tracking?userId={buyerUserId}
    @GetMapping("/api/chat-rooms/{chatRoomId}/tracking")
    public ResponseEntity<TrackingResponseDto> getTracking(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomService.getTracking(chatRoomId, userId));
    }

    // 채팅방 나가기 (구매자 전용)
    // DELETE /api/chat-rooms/{chatRoomId}/leave?userId=X
    @DeleteMapping("/api/chat-rooms/{chatRoomId}/leave")
    public ResponseEntity<Void> leaveChatRoom(
            @PathVariable Long chatRoomId,
            @RequestParam Long userId) {
        chatRoomService.leaveChatRoom(chatRoomId, userId);
        return ResponseEntity.ok().build();
    }

    // WebSocket 메시지 전송
    // 클라이언트 publish: /pub/chat/message
    // 서버 broadcast: /sub/chat/rooms/{chatRoomId}
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequestDto dto) {
        chatMessageService.sendMessage(dto);
    }
}
