package com.gordeok.post.controller;

import com.gordeok.post.dto.PostDetailResponseDto;
import com.gordeok.post.dto.CreatePostRequestDto;
import com.gordeok.post.dto.CreatePostResponseDto;
import com.gordeok.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    // 분철 글 작성
    @PostMapping
    public CreatePostResponseDto createPost(
            @RequestParam Long userId,
            @Valid @RequestBody CreatePostRequestDto request
    ) {
        return postService.createPost(userId, request);
    }

    // 분철 글 상세
    @GetMapping("/{postId}")
    public PostDetailResponseDto getPostDetail(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId
    ) {

        return postService.getPostDetail(postId, userId);
    }
}