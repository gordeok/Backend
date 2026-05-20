package com.gordeok.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePostResponseDto {

    private Long postId;
    private String message; // 성공 안내 문구 출력 -> 나중에 연동 성공하면 없애도 ㄱㅊ음
}