package com.gordeok.auth.dto;
// 로그인 요청값 받기

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;
}
