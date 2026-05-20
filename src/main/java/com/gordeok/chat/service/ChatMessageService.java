package com.gordeok.chat.service;

import com.gordeok.chat.dto.ChatMessageRequestDto;
import com.gordeok.chat.dto.MessageResponseDto;
import com.gordeok.chat.entity.Message;
import com.gordeok.chat.repository.MessageRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendMessage(ChatMessageRequestDto dto) {
        // 1. DB에 메시지 저장
        Message message = Message.builder()
                .chatroomId(dto.getChatRoomId())
                .senderId(dto.getSenderId())
                .content(dto.getContent())
                .messageType(dto.getMessageType() != null ? dto.getMessageType() : "TEXT")
                .build();
        Message saved = messageRepository.save(message);

        // 2. 보낸 사람 닉네임 조회
        User sender = userRepository.findById(dto.getSenderId()).orElse(null);
        String nickname = sender != null ? sender.getNickname() : "알 수 없음";

        // 3. 채팅방 구독자 전체에게 브로드캐스트
        MessageResponseDto response = new MessageResponseDto(saved, nickname);
        messagingTemplate.convertAndSend(
                "/sub/chat/rooms/" + dto.getChatRoomId(), response);
    }
}
