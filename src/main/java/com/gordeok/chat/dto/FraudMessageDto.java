package com.gordeok.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FraudMessageDto {

    @JsonProperty("sender_id")
    private String senderId;

    private String role;      // 판매자 | 구매자

    private String content;

    private String timestamp;
}
