package com.gordeok.post.controller;

import com.gordeok.global.storage.FileStorageService;
import com.gordeok.post.dto.CreatePostRequestDto;
import com.gordeok.post.dto.CreatePostResponseDto;
import com.gordeok.post.dto.ImageAnalyzeResponseDto;
import com.gordeok.post.dto.PostDetailResponseDto;
import com.gordeok.post.dto.PostResponseDto;
import com.gordeok.post.service.PostService;
import com.gordeok.user.dto.MyPostReviewResponseDto;
import com.gordeok.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    // 분철 글 작성
    @PostMapping
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestParam Long userId,
            @Valid @RequestBody CreatePostRequestDto request
    ) {
        return ResponseEntity.ok(postService.createPost(userId, request));
    }

    // 게시글 이미지 업로드 (글 작성 전 선업로드 → URL 반환)
    // POST /api/posts/upload-image
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadPostImage(
            @RequestParam("image") MultipartFile image) {
        String imageUrl = fileStorageService.store(image, "posts");
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    // 게시글 목록 조회 (홈화면)
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String idolName,
            @RequestParam(defaultValue = "latest") String sort) {
        return ResponseEntity.ok(postService.getPostList(page, size, keyword, idolName, sort));
    }

    // 분철 글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPostDetail(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(postService.getPostDetail(postId, userId));
    }

    // AI 이미지 분석
    @PostMapping("/analyze-image")
    public ResponseEntity<ImageAnalyzeResponseDto> analyzeImage(
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(postService.analyzeImage(image));
    }

    // ── MY-08: 특정 게시글 받은 후기 목록 ──
    @GetMapping("/{postId}/reviews")
    public ResponseEntity<List<MyPostReviewResponseDto>> getReviewsByPost(
            @PathVariable Long postId) {
        return ResponseEntity.ok(userService.getReviewsByPost(postId));
    }
}
