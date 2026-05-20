package com.gordeok.auth.dto;
// 회원가입 응답값 보내기

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponseDto {

    private Long userId;
    private String message;
}
