package com.gordeok.participation.service;

import com.gordeok.participation.dto.CreateParticipationRequestDto;
import com.gordeok.participation.dto.CreateParticipationResponseDto;
import com.gordeok.participation.entity.Participation;
import com.gordeok.participation.repository.ParticipationRepository;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateParticipationResponseDto createParticipation(
            Long postId,
            Long memberItemId,
            Long userId,
            CreateParticipationRequestDto request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글을 찾을 수 없습니다.")
                );

        MemberItem memberItem = memberItemRepository.findById(memberItemId)
                .orElseThrow(() ->
                        new IllegalArgumentException("멤버를 찾을 수 없습니다.")
                );

        if (!memberItem.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시글의 멤버가 아닙니다.");
        }

        // 참여글 작성은 모집중 상태에서만 가능
        if (!memberItem.getStatus().equals("AVAILABLE")) {
            throw new IllegalArgumentException("이미 선택된 멤버입니다.");
        }

        if (participationRepository.existsByMemberItemId(memberItemId)) {
            throw new IllegalArgumentException("이미 참여글이 작성된 멤버입니다.");
        }

        // 참여글 작성 시 모집중 → 예약중
        memberItem.reserve(user);

        Participation participation = Participation.builder()
                .post(post)
                .memberItem(memberItem)
                .buyer(user)
                .realName(request.getRealName())
                .phoneNumber(request.getPhoneNumber())
                .storeName(request.getStoreName())
                .requestMessage(request.getRequestMessage())
                .build();

        Participation savedParticipation = participationRepository.save(participation);

        // 참여글 작성 단계에서는 아직 채팅방 입장 전이므로 null
        Long chatRoomId = null;

        return new CreateParticipationResponseDto(
                savedParticipation.getId(),
                chatRoomId,
                memberItem.getStatus(),
                "분철 참여글이 작성되었습니다."
        );
    }
}