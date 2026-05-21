package com.gordeok.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSelectRequestDto {
    private Long buyerId;
    private String recipientName;   // 받으시는 분
    private String phoneNumber;     // 전화번호
    private String convenienceStore; // 편의점 지점명
    private String request;          // 요청사항 (선택)
}
