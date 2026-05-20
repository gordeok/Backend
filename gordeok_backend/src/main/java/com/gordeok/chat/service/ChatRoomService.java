package com.gordeok.chat.service;

import com.gordeok.chat.dto.ChatRoomListResponseDto;
import com.gordeok.chat.dto.MessageResponseDto;
import com.gordeok.chat.entity.ChatParticipant;
import com.gordeok.chat.entity.ChatRoom;
import com.gordeok.chat.entity.Message;
import com.gordeok.chat.repository.ChatParticipantRepository;
import com.gordeok.chat.repository.ChatRoomRepository;
import com.gordeok.chat.repository.MessageRepository;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // 내 채팅방 목록 조회
    public List<ChatRoomListResponseDto> getMyChatRooms(Long userId) {
        List<ChatParticipant> myParticipations = chatParticipantRepository.findByUserId(userId);

        return myParticipations.stream().map(participant -> {
            ChatRoom chatRoom = chatRoomRepository.findById(participant.getChatroomId())
                    .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

            Post post = postRepository.findById(chatRoom.getPostId())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            // 채팅방 제목: 아이돌명 + 앨범명
            String title = post.getIdolName() + " " + post.getAlbumName();

            // 판매자 닉네임
            String sellerName = post.getUser().getNickname();

            // 현재 참여 인원 (판매자 포함)
            int currentMembers = chatParticipantRepository.countByChatroomId(chatRoom.getId());

            // 정원: 해당 post의 멤버 아이템 수
            int maxMembers = memberItemRepository.findByPostId(post.getId()).size();

            // 마지막 메시지
            Message lastMsg = messageRepository
                    .findTopByChatroomIdOrderByCreatedAtDesc(chatRoom.getId())
                    .orElse(null);
            String lastMessage = lastMsg != null ? lastMsg.getContent() : "";

            // 읽지 않은 메시지 수
            int unreadCount = 0;
            if (lastMsg != null) {
                Long lastReadId = participant.getLastReadMessageId() != null
                        ? participant.getLastReadMessageId() : 0L;
                unreadCount = messageRepository
                        .countByChatroomIdAndIdGreaterThan(chatRoom.getId(), lastReadId);
            }

            // Post status를 프론트 표기로 변환 (OPEN → progress, COMPLETED → done)
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
}
