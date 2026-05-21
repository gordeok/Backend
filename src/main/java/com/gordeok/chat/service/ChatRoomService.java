package com.gordeok.chat.service;

import com.gordeok.chat.dto.*;
import com.gordeok.chat.dto.TrackingSetupBuyerDto;
import com.gordeok.chat.dto.TrackingSetupResponseDto;
import com.gordeok.chat.entity.ChatParticipant;
import com.gordeok.chat.entity.ChatRoom;
import com.gordeok.chat.entity.Message;
import com.gordeok.chat.repository.ChatParticipantRepository;
import com.gordeok.chat.repository.ChatRoomRepository;
import com.gordeok.chat.repository.MessageRepository;
import com.gordeok.participation.entity.Participation;
import com.gordeok.participation.repository.ParticipationRepository;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // 내 채팅방 목록 조회
    public List<ChatRoomListResponseDto> getMyChatRooms(Long userId) {
        List<ChatParticipant> myParticipations = chatParticipantRepository.findByUserId(userId);

        return myParticipations.stream().map(participant -> {
            ChatRoom chatRoom = chatRoomRepository.findById(participant.getChatroomId())
                    .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

            Post post = postRepository.findById(chatRoom.getPostId())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            String title = post.getIdolName() + " " + post.getAlbumName();
            String sellerName = post.getUser().getNickname();
            int currentMembers = chatParticipantRepository.countByChatroomId(chatRoom.getId());
            int maxMembers = memberItemRepository.findByPostId(post.getId()).size();

            Message lastMsg = messageRepository
                    .findTopByChatroomIdOrderByCreatedAtDesc(chatRoom.getId())
                    .orElse(null);
            String lastMessage = lastMsg != null ? lastMsg.getContent() : "";

            int unreadCount = 0;
            if (lastMsg != null) {
                Long lastReadId = participant.getLastReadMessageId() != null
                        ? participant.getLastReadMessageId() : 0L;
                unreadCount = messageRepository
                        .countByChatroomIdAndIdGreaterThan(chatRoom.getId(), lastReadId);
            }

            String status = "COMPLETED".equals(post.getStatus()) ? "done" : "progress";

            return ChatRoomListResponseDto.builder()
                    .chatRoomId(chatRoom.getId())
                    .title(title)
                    .sellerName(sellerName)
                    .currentMembers(currentMembers)
                    .maxMembers(maxMembers)
                    .lastMessage(lastMessage)
                    .unreadCount(unreadCount)
                    .lastMessageTime(lastMsg != null ? lastMsg.getCreatedAt() : null)
                    .status(status)
                    .myRole(participant.getRole())
                    .type(chatRoom.getType())
                    .build();
        }).collect(Collectors.toList());
    }

    // 채팅 메시지 목록 조회
    public List<MessageResponseDto> getMessages(Long chatRoomId) {
        List<Message> messages = messageRepository
                .findByChatroomIdOrderByCreatedAtAsc(chatRoomId);

        return messages.stream().map(message -> {
            User sender = userRepository.findById(message.getSenderId()).orElse(null);
            String nickname = sender != null ? sender.getNickname() : "알 수 없음";
            return new MessageResponseDto(message, nickname);
        }).collect(Collectors.toList());
    }

    // 채팅 메뉴 조회
    public ChatMenuResponseDto getChatMenu(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Post post = postRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        String title = post.getIdolName() + " " + post.getAlbumName();

        // 모집 상태 계산: 모든 슬롯이 COMPLETED면 "모집완료", 하나라도 AVAILABLE/RESERVED면 "모집중"
        List<com.gordeok.post.entity.MemberItem> memberItems = memberItemRepository.findByPostId(post.getId());
        boolean allCompleted = !memberItems.isEmpty() &&
                memberItems.stream().allMatch(item -> "COMPLETED".equals(item.getStatus()));
        String recruitStatus = allCompleted ? "모집완료" : "모집중";

        // 내 role 조회
        ChatParticipant me = chatParticipantRepository.findByChatroomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방 참여자가 아닙니다."));

        // 본인 제외 대화상대 목록
        List<ChatParticipant> others = chatParticipantRepository.findByChatroomId(chatRoomId)
                .stream()
                .filter(p -> !p.getUserId().equals(userId))
                .collect(Collectors.toList());

        List<ChatMenuParticipantDto> participants = others.stream().map(p -> {
            User user = userRepository.findById(p.getUserId()).orElse(null);
            String nickname = user != null ? user.getNickname() : "알 수 없음";

            // 구매자인 경우 선택한 멤버 이름 조회
            String memberName = "";
            if ("BUYER".equals(p.getRole())) {
                Participation participation = participationRepository
                        .findByBuyerIdAndPostId(p.getUserId(), post.getId())
                        .orElse(null);
                if (participation != null && participation.getMemberItem() != null) {
                    memberName = participation.getMemberItem().getMemberName();
                }
            }

            return ChatMenuParticipantDto.builder()
                    .userId(p.getUserId())
                    .nickname(nickname)
                    .memberName(memberName)
                    .role(p.getRole())
                    .build();
        }).collect(Collectors.toList());

        return ChatMenuResponseDto.builder()
                .chatRoomId(chatRoomId)
                .title(title)
                .postStatus(recruitStatus)  // "모집중" or "모집완료"
                .myRole(me.getRole())
                .participants(participants)
                .build();
    }

    // 구매자 정보 확인 (판매자 전용)
    public BuyerInfoResponseDto getBuyerInfo(Long chatRoomId, Long buyerUserId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Participation participation = participationRepository
                .findByBuyerIdAndPostId(buyerUserId, chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));

        User buyer = userRepository.findById(buyerUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        String memberName = participation.getMemberItem() != null
                ? participation.getMemberItem().getMemberName() : "";

        return BuyerInfoResponseDto.builder()
                .nickname(buyer.getNickname())
                .memberName(memberName)
                .realName(participation.getRealName())
                .phoneNumber(participation.getPhoneNumber())
                .storeName(participation.getStoreName())
                .requestMessage(participation.getRequestMessage())
                .build();
    }

    // 거래 완료 (판매자 전용)
    @Transactional
    public void completeTransaction(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Post post = postRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.complete();
        postRepository.save(post);

        // WebSocket으로 채팅방 전체에 거래 완료 알림 전송
        messagingTemplate.convertAndSend(
                "/sub/chat/rooms/" + chatRoomId,
                MessageResponseDto.systemMessage(chatRoomId, "거래가 완료되었습니다.", "TRANSACTION_COMPLETE")
        );
    }

    // 거래 취소 (판매자 전용)
    @Transactional
    public void cancelTransaction(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Post post = postRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.close();
        postRepository.save(post);
    }

    // 운송장 공유 페이지 초기 데이터 조회 (판매자 전용)
    // post.shippingFeeType + 구매자 목록(닉네임, 멤버명) 반환
    public TrackingSetupResponseDto getTrackingSetup(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Post post = postRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 채팅방의 구매자 목록 조회
        List<TrackingSetupBuyerDto> buyers = chatParticipantRepository
                .findByChatroomId(chatRoomId)
                .stream()
                .filter(p -> "BUYER".equals(p.getRole()))
                .map(p -> {
                    User user = userRepository.findById(p.getUserId()).orElse(null);
                    String nickname = user != null ? user.getNickname() : "알 수 없음";

                    // 선택한 멤버명 조회
                    String memberName = "";
                    Participation participation = participationRepository
                            .findByBuyerIdAndPostId(p.getUserId(), post.getId())
                            .orElse(null);
                    if (participation != null && participation.getMemberItem() != null) {
                        memberName = participation.getMemberItem().getMemberName();
                    }

                    return TrackingSetupBuyerDto.builder()
                            .buyerUserId(p.getUserId())
                            .nickname(nickname)
                            .memberName(memberName)
                            .build();
                })
                .collect(Collectors.toList());

        return TrackingSetupResponseDto.builder()
                .defaultCourierType(post.getShippingFeeType()) // 분철글에 등록된 배송 방법
                .buyers(buyers)
                .build();
    }

    // 운송장 번호 공유 (판매자 전용)
    // 구매자별로 택배사 + 운송장 번호 저장하고 WebSocket으로 각 구매자에게 알림
    @Transactional
    public void shareTracking(Long chatRoomId, TrackingShareRequestDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        for (TrackingShareItemDto item : dto.getTrackingList()) {
            // 운송장 번호가 입력된 경우만 처리
            if (item.getTrackingNumber() == null || item.getTrackingNumber().isBlank()) continue;

            Participation participation = participationRepository
                    .findByBuyerIdAndPostId(item.getBuyerUserId(), chatRoom.getPostId())
                    .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));

            participation.updateTracking(item.getCourierType(), item.getTrackingNumber());
            participationRepository.save(participation);

            // WebSocket으로 해당 구매자에게 배송 알림 전송
            messagingTemplate.convertAndSend(
                    "/sub/chat/rooms/" + chatRoomId,
                    MessageResponseDto.systemMessage(chatRoomId,
                            item.getCourierType() + " 운송장 번호가 공유되었습니다: " + item.getTrackingNumber(),
                            "TRACKING_SHARED")
            );
        }
    }

    // 배송 현황 조회 (구매자 전용)
    // 자신의 운송장 번호 + 택배 조회 URL 반환
    public TrackingResponseDto getTracking(Long chatRoomId, Long buyerUserId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Participation participation = participationRepository
                .findByBuyerIdAndPostId(buyerUserId, chatRoom.getPostId())
                .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));

        String trackingUrl = buildTrackingUrl(participation.getCourierType(), participation.getTrackingNumber());

        return TrackingResponseDto.builder()
                .courierType(participation.getCourierType())
                .trackingNumber(participation.getTrackingNumber())
                .trackingUrl(trackingUrl)
                .build();
    }

    // 채팅방 나가기 (구매자 전용)
    // 1. participation 삭제
    // 2. member_item → AVAILABLE 복구
    // 3. chat_participants에서 제거
    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        ChatParticipant participant = chatParticipantRepository
                .findByChatroomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방 참여자가 아닙니다."));

        // 구매자인 경우에만 participation 삭제 + 멤버 슬롯 복구
        if ("BUYER".equals(participant.getRole())) {
            Participation participation = participationRepository
                    .findByBuyerIdAndPostId(userId, chatRoom.getPostId())
                    .orElseThrow(() -> new RuntimeException("참여 정보를 찾을 수 없습니다."));

            // 선택했던 멤버 슬롯 → AVAILABLE 복구
            MemberItem memberItem = participation.getMemberItem();
            MemberItem restored = MemberItem.builder()
                    .id(memberItem.getId())
                    .post(memberItem.getPost())
                    .memberName(memberItem.getMemberName())
                    .price(memberItem.getPrice())
                    .status("AVAILABLE")
                    .build();
            memberItemRepository.save(restored);

            // participation 삭제
            participationRepository.delete(participation);
        }

        // chat_participants에서 제거
        chatParticipantRepository.deleteByChatroomIdAndUserId(chatRoomId, userId);
    }

    // 택배사별 조회 URL 생성
    private String buildTrackingUrl(String courierType, String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) return null;
        if (courierType == null) return null;

        return switch (courierType) {
            case "GS반값택배" -> "https://trace.cjlogistics.com/web/detail.jsp?slipno=" + trackingNumber;
            case "CU반값택배" -> "https://trace.cjlogistics.com/web/detail.jsp?slipno=" + trackingNumber;
            default -> "https://trace.cjlogistics.com/web/detail.jsp?slipno=" + trackingNumber;
        };
    }
}
