package com.gordeok.community.controller;

import com.gordeok.community.dto.*;
import com.gordeok.community.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    // 커뮤니티 글 목록 조회
    // GET /api/community/posts?category=ALL&sort=latest&page=0&size=10
    @GetMapping("/posts")
    public ResponseEntity<Page<CommunityPostListResponseDto>> getPostList(
            @RequestParam(defaultValue = "ALL") String category,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                communityService.getPostList(category, sort, page, size)
        );
    }

    // 커뮤니티 글 상세 조회
    // GET /api/community/posts/1
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommunityPostDetailResponseDto> getPostDetail(
            @PathVariable Long postId,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        return ResponseEntity.ok(
                communityService.getPostDetail(postId, userId)
        );
    }

    // 커뮤니티 글 작성
    // POST /api/community/posts
    @PostMapping("/posts")
    public ResponseEntity<CreateCommunityPostResponseDto> createPost(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody CreateCommunityPostRequestDto request
    ) {
        return ResponseEntity.ok(
                communityService.createPost(userId, request)
        );
    }

    // 댓글 작성
    // POST /api/community/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommunityCommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody CreateCommunityCommentRequestDto request
    ) {
        return ResponseEntity.ok(
                communityService.createComment(postId, userId, request)
        );
    }

    // 좋아요 추가 / 취소
    // POST /api/community/posts/1/likes/toggle
    @PostMapping("/posts/{postId}/likes/toggle")
    public ResponseEntity<ToggleLikeResponseDto> toggleLike(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ResponseEntity.ok(
                communityService.toggleLike(postId, userId)
        );
    }
}