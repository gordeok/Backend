package com.gordeok.post.controller;

import com.gordeok.post.dto.CreatePostRequestDto;
import com.gordeok.post.dto.CreatePostResponseDto;
import com.gordeok.post.dto.ImageAnalyzeResponseDto;
import com.gordeok.post.dto.PostDetailResponseDto;
import com.gordeok.post.dto.PostResponseDto;
import com.gordeok.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 분철 글 작성
    @PostMapping
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestParam Long userId,
            @Valid @RequestBody CreatePostRequestDto request
    ) {
        return ResponseEntity.ok(postService.createPost(userId, request));
    }

    // 게시글 목록 조회 (홈화면 - 검색, 아이돌 필터, 정렬 포함)
    // GET /api/posts?page=0&size=10&keyword=검색어&idolName=BOYNEXTDOOR&sort=latest
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

    @PostMapping("/analyze-image")
    public ResponseEntity<ImageAnalyzeResponseDto> analyzeImage(
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return ResponseEntity.ok(postService.analyzeImage(image));
    }
}
