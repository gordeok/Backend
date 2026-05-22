package com.gordeok.user.service;

import com.gordeok.community.entity.CommunityPost;
import com.gordeok.community.repository.CommunityPostRepository;
import com.gordeok.participation.entity.Participation;
import com.gordeok.participation.repository.ParticipationRepository;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.report.repository.ReportRepository;
import com.gordeok.review.entity.Review;
import com.gordeok.review.repository.ReviewRepository;
import com.gordeok.user.dto.*;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;
    private final ReportRepository reportRepository;
    private final CommunityPostRepository communityPostRepository;

    // ── MY-01: 내 프로필 조회 ──
    public MyProfileResponseDto getMyProfile(Long userId) {
        User user = findUserById(userId);
        return new MyProfileResponseDto(user);
    }

    // ── MY-02: 타 유저 프로필 조회 (판매자 프로필 창) ──
    public UserProfileResponseDto getUserProfile(Long userId) {
        User user = findUserById(userId);
        return new UserProfileResponseDto(user);
    }

    // ── MY-03: 닉네임 수정 ──
    @Transactional
    public UpdateNicknameResponseDto updateNickname(Long userId, UpdateNicknameRequestDto request) {
        User user = findUserById(userId);

        // 본인 제외 중복 확인
        if (userRepository.existsByNicknameAndIdNot(request.getNickname(), userId)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.updateNickname(request.getNickname());
        return new UpdateNicknameResponseDto(user.getId(), user.getNickname(), "닉네임이 변경되었습니다.");
    }

    // ── MY-04: 프로필 사진 업데이트 (URL 직접 저장 방식 - S3 업로드는 Controller에서 처리) ──
    @Transactional
    public UpdateProfileImageResponseDto updateProfileImage(Long userId, String imageUrl) {
        User user = findUserById(userId);
        user.updateProfileImage(imageUrl);
        return new UpdateProfileImageResponseDto(imageUrl, "프로필 사진이 변경되었습니다.");
    }

    // ── MY-05: 분철 구매 목록 (Post.status 기준) ──
    public Page<PurchaseListResponseDto> getPurchaseList(Long userId, String status, Pageable pageable) {
        // status 값 정규화: "OPEN" or "COMPLETED"
        String postStatus = resolvePostStatus(status);

        Page<Participation> participations = participationRepository
                .findByBuyerIdAndPostStatus(userId, postStatus, pageable);

        return participations.map(p -> {
            // 후기 작성 가능 여부: 거래 완료 && 아직 후기 미작성
            boolean canWriteReview = false;
            if ("COMPLETED".equals(postStatus)) {
                canWriteReview = !reviewRepository.existsByReviewerIdAndPostId(userId, p.getPost().getId());
            }
            return new PurchaseListResponseDto(p, canWriteReview);
        });
    }

    // ── MY-07: 분철 판매 목록 (Post.status 기준) ──
    public Page<SaleListResponseDto> getSaleList(Long userId, String status, Pageable pageable) {
        String postStatus = resolvePostStatus(status);

        Page<Post> posts = postRepository
                .findByUserIdAndStatusOrderByCreatedAtDesc(userId, postStatus, pageable);

        return posts.map(post -> {
            Long count = postRepository.countParticipationsByPostId(post.getId());
            return new SaleListResponseDto(post, count);
        });
    }

    // ── MY-08: 판매 완료 건의 받은 후기 (chatRoomId 기준 조회, postId로 chatRoom 탐색) ──
    // 단순화: targetUserId = 판매자(me), reviewerId가 해당 post 참여자인 후기를 조회
    public List<MyPostReviewResponseDto> getReviewsByPost(Long postId) {
        // 해당 post의 Participation 목록에서 chatRoomId 수집 후 후기 조회
        // chatRoomId 기반으로 후기 조회 (Participation.chatRoomId는 아직 null일 수 있으므로 대안 사용)
        // 대안: post의 판매자를 targetUserId로 하고, reviewerID가 해당 post 참여자인 후기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Long sellerId = post.getUser().getId();

        // targetUserId = sellerId 인 후기 중 reviewer가 해당 post 참여자인 것
        List<Review> allSellerReviews = reviewRepository.findByTargetUserId(sellerId);

        return allSellerReviews.stream()
                .filter(review -> {
                    // reviewerId 가 해당 post 에 참여한 구매자인지 확인
                    return participationRepository
                            .findAllByBuyerIdAndPostId(review.getReviewerId(), postId)
                            .stream().findAny().isPresent();
                })
                .map(review -> {
                    User reviewer = userRepository.findById(review.getReviewerId()).orElse(null);
                    return new MyPostReviewResponseDto(review, reviewer);
                })
                .collect(Collectors.toList());
    }

    // ── MY-10: 내가 작성한 커뮤니티 글 ──
    public Page<MyCommunityPostResponseDto> getMyCommunityPosts(Long userId, Pageable pageable) {
        return communityPostRepository
                .findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(MyCommunityPostResponseDto::new);
    }

    // ── MY-11: 받은 후기 전체 목록 ──
    public Page<MyPostReviewResponseDto> getReceivedReviews(Long userId, Pageable pageable) {
        Page<Review> reviews = reviewRepository
                .findByTargetUserIdOrderByCreatedAtDesc(userId, pageable);

        return reviews.map(review -> {
            User reviewer = userRepository.findById(review.getReviewerId()).orElse(null);
            return new MyPostReviewResponseDto(review, reviewer);
        });
    }

    // ── MY-14: 신뢰 점수 상세 (산정 로직) ──
    public TrustScoreResponseDto getTrustScore(Long userId) {
        User user = findUserById(userId);

        // 거래 완료율: 내 구매 건 중 COMPLETED 비율
        long totalPurchases = participationRepository.countByBuyerId(userId);
        long completedPurchases = participationRepository.countByBuyerIdAndPostStatus(userId, "COMPLETED");
        double completeRate = totalPurchases > 0
                ? (double) completedPurchases / totalPurchases : 0.0;

        // 신고 건수
        long reportCount = reportRepository.countByTargetUserId(userId);

        // 채팅 응답 속도 점수: 현재 데이터 없으므로 trustScore에서 역산 (임시)
        int chatScore = Math.max(0, user.getTrustScore() - (int)(completeRate * 50));

        TrustScoreResponseDto.Breakdown breakdown = TrustScoreResponseDto.Breakdown.builder()
                .transactionCompleteRate(completeRate)
                .chatResponseSpeed(chatScore)
                .reportCount((int) reportCount)
                .build();

        return TrustScoreResponseDto.builder()
                .totalScore(user.getTrustScore())
                .breakdown(breakdown)
                .build();
    }

    // ── 판매자 프로필용: 최근 게시글 4개 ──
    public List<Post> getRecentPostsByUser(Long userId) {
        return postRepository.findTop4ByUserIdOrderByCreatedAtDesc(userId);
    }

    // ── 내부 헬퍼 ──
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private String resolvePostStatus(String status) {
        if (status == null || status.isBlank()) return "OPEN";
        return switch (status.toUpperCase()) {
            case "COMPLETED" -> "COMPLETED";
            default -> "OPEN";
        };
    }
}
