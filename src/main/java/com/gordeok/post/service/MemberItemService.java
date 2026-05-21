package com.gordeok.post.service;

import com.gordeok.chat.entity.ChatParticipant;
import com.gordeok.chat.entity.ChatRoom;
import com.gordeok.chat.repository.ChatParticipantRepository;
import com.gordeok.chat.repository.ChatRoomRepository;
import com.gordeok.participation.entity.Participation;
import com.gordeok.participation.repository.ParticipationRepository;
import com.gordeok.post.dto.MemberSelectRequestDto;
import com.gordeok.post.dto.MemberSelectResponseDto;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberItemService {

    private final MemberItemRepository memberItemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;

    // [1단계] 멤버 슬롯 클릭 → AVAILABLE → RESERVED (슬롯 잠금만)
    // POST /api/member-items/{memberItemId}/reserve?buyerId=X
    @Transactional
    public void reserveSlot(Long memberItemId, Long buyerId) {
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        if (!"AVAILABLE".equals(memberItem.getStatus())) {
            throw new RuntimeException("이미 선택된 멤버 슬롯입니다.");
        }

        MemberItem reserved = MemberItem.builder()
                .id(memberItem.getId())
                .post(memberItem.getPost())
                .memberName(memberItem.getMemberName())
                .price(memberItem.getPrice())
                .status("RESERVED")
                .build();
        memberItemRepository.save(reserved);
    }

    // [2단계] 참여글 작성 완료 + 채팅방 입장 버튼 → RESERVED → COMPLETED + 참여글 생성 + 채팅방 생성/입장
    // POST /api/member-items/{memberItemId}/select
    @Transactional
    public MemberSelectResponseDto selectMember(Long memberItemId, MemberSelectRequestDto dto) {
        // 1. MemberItem 조회 및 상태 확인 (RESERVED 상태여야 함)
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        if (!"RESERVED".equals(memberItem.getStatus())) {
            throw new RuntimeException("예약 중인 슬롯이 아닙니다.");
        }

        // 2. MemberItem status → COMPLETED (모집완료)
        memberItem.complete();
        memberItemRepository.save(memberItem);

        // 3. Participation 생성 (구매자 배송 정보 저장)
        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Participation participation = Participation.builder()
                .post(memberItem.getPost())
                .memberItem(memberItem)
                .buyer(buyer)
                .realName(dto.getRecipientName())
                .phoneNumber(dto.getPhoneNumber())
                .storeName(dto.getConvenienceStore())
                .requestMessage(dto.getRequest())
                .build();
        participationRepository.save(participation);

        // 4. 해당 Post의 ChatRoom 조회 또는 생성
        Long postId = memberItem.getPost().getId();
        ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .postId(postId)
                                .type("GROUP")
                                .build()
                ));

        // 5. 판매자를 SELLER로 추가 (처음 한 번만)
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

        // 6. 구매자를 BUYER로 추가
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

    // 멤버 선택 취소
    @Transactional
    public void cancelMember(Long memberItemId, Long buyerId) {
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        if (!"RESERVED".equals(memberItem.getStatus())) {
            throw new RuntimeException("예약 중인 슬롯이 아닙니다.");
        }

        // status → AVAILABLE 복구
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
