package com.gordeok.review.repository;

import com.gordeok.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 기존
    List<Review> findByTargetUserId(Long targetUserId);
    boolean existsByReviewerIdAndChatRoomId(Long reviewerId, Long chatRoomId);

    // 마이페이지: 받은 후기 목록 (페이징)
    Page<Review> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId, Pageable pageable);

    // 판매 목록: 특정 post 에 대한 후기 목록
    // Participation → chatRoomId 연결이므로, reviewerId가 해당 post 참여자인 후기를 조회
    // 단순화: chatRoomId로 후기 조회 (채팅방 기준)
    List<Review> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    // 후기 총 개수
    long countByTargetUserId(Long targetUserId);

    // 구매자가 이미 후기를 작성했는지 (postId 연계: chatRoomId 없을 때 대비)
    @Query("SELECT COUNT(r) > 0 FROM Review r " +
           "JOIN Participation p ON p.buyer.id = r.reviewerId " +
           "WHERE r.reviewerId = :reviewerId AND p.post.id = :postId")
    boolean existsByReviewerIdAndPostId(@Param("reviewerId") Long reviewerId,
                                        @Param("postId") Long postId);
}
