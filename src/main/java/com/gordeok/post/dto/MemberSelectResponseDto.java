package com.gordeok.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSelectResponseDto {
    private Long chatRoomId;
    private String message;
}
