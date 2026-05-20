package com.gordeok.auth.dto;
// 회원가입 요청값 받기

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String nickname;
    private String email;
    private String password;
    private String passwordConfirm;
}