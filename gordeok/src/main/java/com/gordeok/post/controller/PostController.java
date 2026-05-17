package com.gordeok.post.controller;

import com.gordeok.post.dto.CreatePostRequestDto;
import com.gordeok.post.dto.CreatePostResponseDto;
import com.gordeok.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public CreatePostResponseDto createPost(
            @RequestParam Long userId,
            @RequestBody CreatePostRequestDto request
    ) {

        return postService.createPost(userId, request);
    }
}