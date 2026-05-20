package com.gordeok.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommunityPostResponseDto {

    private Long postId;
    private String message;
}