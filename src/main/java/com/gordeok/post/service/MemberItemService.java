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
    private final ParticipationRepository participationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    // 멤버 자리 선택 → 참여글 저장 → 채팅방 생성/입장
    @Transactional
    public MemberSelectResponseDto selectMember(Long memberItemId, MemberSelectRequestDto dto) {
        // 1. MemberItem 조회 및 상태 확인
        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() -> new RuntimeException("멤버 슬롯을 찾을 수 없습니다."));

        if (!"AVAILABLE".equals(memberItem.getStatus())) {
            throw new RuntimeException("이미 선택된 멤버 슬롯입니다.");
        }

        // 2. 구매자 User 조회
        User buyer = userRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 3. MemberItem status → RESERVED + buyer 설정
        memberItem.reserve(buyer);
        memberItemRepository.save(memberItem);

        // 4. 참여글 정보 저장 (팀원 Participation 구조 사용)
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

        // 5. 해당 Post의 ChatRoom 조회 또는 생성
        Long postId = memberItem.getPost().getId();
        ChatRoom chatRoom = chatRoomRepository.findByPostId(postId)
                .orElseGet(() -> chatRoomRepository.save(
                        ChatRoom.builder()
                                .postId(postId)
                                .type("GROUP")
                                .build()
                ));

        // 6. 판매자를 SELLER로 추가 (처음 한 번만)
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

        // 7. 구매자를 BUYER로 추가
        if (!chatParticipantRepository.existsByChatroomIdAndUserId(chatRoom.getId(), buyer.getId())) {
            chatParticipantRepository.save(
                    ChatParticipant.builder()
                            .chatroomId(chatRoom.getId())
                            .userId(buyer.getId())
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
        memberItem.cancel();
        memberItemRepository.save(memberItem);

        // 참여글 삭제
        participationRepository.findByMemberItem(memberItem)
                .ifPresent(participationRepository::delete);
    }
}
