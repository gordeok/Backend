package com.gordeok.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateNicknameResponseDto {
    private Long userId;
    private String nickname;
    private String message;
}
