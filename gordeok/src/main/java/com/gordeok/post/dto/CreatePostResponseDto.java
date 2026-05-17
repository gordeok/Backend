package com.gordeok.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostResponseDto {

    private Long postId;

    private String message;
}