package com.gordeok.chat.service;

import com.gordeok.chat.dto.*;
import com.gordeok.chat.entity.ChatParticipant;
import com.gordeok.chat.entity.Message;
import com.gordeok.chat.repository.ChatParticipantRepository;
import com.gordeok.chat.repository.MessageRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatParticipantRepository chatParticipantRepository;
    private final FraudDetectionClient fraudDetectionClient;

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

        // 4. 사기 탐지 비동기 호출 (메시지 전송 지연 없음)
        analyzeFraud(dto.getChatRoomId());
    }

    private void analyzeFraud(Long chatRoomId) {
        // 참여자 역할 맵: userId → "판매자" | "구매자"
        Map<Long, String> roleMap = chatParticipantRepository.findByChatroomId(chatRoomId)
                .stream()
                .collect(Collectors.toMap(
                        ChatParticipant::getUserId,
                        p -> "SELLER".equals(p.getRole()) ? "판매자" : "구매자"
                ));

        // 해당 채팅방 전체 메시지 조회 (AI 서버가 내부적으로 최근 20개만 처리)
        List<FraudMessageDto> fraudMessages = messageRepository
                .findByChatroomIdOrderByCreatedAtAsc(chatRoomId)
                .stream()
                .map(m -> new FraudMessageDto(
                        String.valueOf(m.getSenderId()),
                        roleMap.getOrDefault(m.getSenderId(), "구매자"),
                        m.getContent(),
                        m.getCreatedAt().toString()
                ))
                .toList();

        FraudAnalyzeRequestDto request = new FraudAnalyzeRequestDto(
                String.valueOf(chatRoomId),
                fraudMessages
        );

        // 비동기 호출 - 결과 오면 필요 시 배너 브로드캐스트
        fraudDetectionClient.analyze(request)
                .subscribe(result -> {
                    if (result == null) return;

                    String action = result.getAction();
                    if ("show_warning_banner".equals(action)) {
                        FraudAlertDto alert = new FraudAlertDto("FRAUD_WARNING", result.getLlmReason());
                        messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatRoomId, alert);
                        log.info("사기 경고 배너 전송 (WARNING) - chatRoomId: {}", chatRoomId);
                    } else if ("show_danger_banner".equals(action)) {
                        FraudAlertDto alert = new FraudAlertDto("FRAUD_DANGER", result.getLlmReason());
                        messagingTemplate.convertAndSend("/sub/chat/rooms/" + chatRoomId, alert);
                        log.info("사기 위험 배너 전송 (DANGER) - chatRoomId: {}", chatRoomId);
                    }
                });
    }
}
