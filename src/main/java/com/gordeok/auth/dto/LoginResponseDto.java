package com.gordeok.auth.dto;
// 로그인 응답값 보내기

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private Long userId;
    private String nickname;
    private String message;
}