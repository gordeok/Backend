package com.gordeok.post.service;

import com.gordeok.chat.entity.ChatParticipant;
import com.gordeok.chat.entity.ChatRoom;
import com.gordeok.chat.repository.ChatParticipantRepository;
import com.gordeok.chat.repository.ChatRoomRepository;
import com.gordeok.post.dto.MemberSelectRequestDto;
import com.gordeok.post.dto.MemberSelectResponseDto;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.repository.MemberItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberItemService {

    private final MemberItemRepository memberItemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    // 채팅방 입장 → COMPLETED(모집완료)로 전환
    @Transactional
    public MemberSelectResponseDto selectMember(Long memberItemId, MemberSelectRequestDto dto) {
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        // 참여글이 작성된(RESERVED) 상태에서만 채팅방 입장 가능
        if (!"RESERVED".equals(memberItem.getStatus())) {
            throw new RuntimeException("참여글이 작성된 상태에서만 채팅방에 입장할 수 있습니다.");
        }

        // 채팅방 입장 시 COMPLETED(모집완료)로 변경
        memberItem.complete();
        memberItemRepository.save(memberItem);

        Long postId = memberItem.getPost().getId();
        ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .postId(postId)
                                .type("GROUP")
                                .build()
                ));

        Long sellerId = memberItem.getPost().getUser().getId();
        if (!chatParticipantRepository.existsByChatroomIdAndUserId(chatRoom.getId(), sellerId)) {
            chatParticipantRepository.save(
                    ChatParticipant.builder()
                            .chatroomId(chatRoom.getId())
                            .userId(sellerId)
                            .role("SELLER")
                            .build()
            );
        }

        if (!chatParticipantRepository.existsByChatroomIdAndUserId(chatRoom.getId(), dto.getBuyerId())) {
            chatParticipantRepository.save(
                    ChatParticipant.builder()
                            .chatroomId(chatRoom.getId())
                            .userId(dto.getBuyerId())
                            .role("BUYER")
                            .build()
            );
        }

        return new MemberSelectResponseDto(chatRoom.getId(), "채팅방에 입장했습니다.");
    }

    // 멤버 선택 취소 (RESERVED → AVAILABLE 복구)
    @Transactional
    public void cancelMember(Long memberItemId, Long buyerId) {
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        if (!"RESERVED".equals(memberItem.getStatus())) {
            throw new RuntimeException("예약 중인 슬롯이 아닙니다.");
        }

        MemberItem cancelled = MemberItem.builder()
                .id(memberItem.getId())
                .post(memberItem.getPost())
                .memberName(memberItem.getMemberName())
                .price(memberItem.getPrice())
                .status("AVAILABLE")
                .build();
        memberItemRepository.save(cancelled);
    }
}