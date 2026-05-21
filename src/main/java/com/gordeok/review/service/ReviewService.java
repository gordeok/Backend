package com.gordeok.review.service;

import com.gordeok.review.dto.CreateReviewRequestDto;
import com.gordeok.review.dto.CreateReviewResponseDto;
import com.gordeok.review.entity.Review;
import com.gordeok.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public CreateReviewResponseDto createReview(CreateReviewRequestDto dto) {
        // 이미 후기를 작성했는지 확인
        if (reviewRepository.existsByReviewerIdAndChatRoomId(dto.getReviewerId(), dto.getChatRoomId())) {
            throw new RuntimeException("이미 후기를 작성하셨습니다.");
        }

        Review review = Review.builder()
                .reviewerId(dto.getReviewerId())
                .targetUserId(dto.getTargetUserId())
                .chatRoomId(dto.getChatRoomId())
                .rating(dto.getRating())
                .content(dto.getContent())
                .build();

        Review saved = reviewRepository.save(review);
        return new CreateReviewResponseDto(saved.getId(), "후기가 등록되었습니다.");
    }
}
