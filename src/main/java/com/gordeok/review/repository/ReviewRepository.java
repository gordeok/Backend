package com.gordeok.review.repository;

import com.gordeok.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetUserId(Long targetUserId);
    boolean existsByReviewerIdAndChatRoomId(Long reviewerId, Long chatRoomId);
}
