package com.gordeok.review.controller;

import com.gordeok.review.dto.CreateReviewRequestDto;
import com.gordeok.review.dto.CreateReviewResponseDto;
import com.gordeok.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 거래 후기 작성
    // POST /api/reviews
    @PostMapping
    public ResponseEntity<CreateReviewResponseDto> createReview(
            @RequestBody CreateReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.createReview(dto));
    }
}
