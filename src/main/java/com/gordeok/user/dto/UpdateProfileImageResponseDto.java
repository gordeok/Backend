package com.gordeok.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileImageResponseDto {
    private String profileImageUrl;
    private String message;
}
