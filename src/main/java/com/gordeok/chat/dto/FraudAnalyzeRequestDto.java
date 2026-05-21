package com.gordeok.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FraudAnalyzeRequestDto {

    @JsonProperty("room_id")
    private String roomId;

    private List<FraudMessageDto> messages;
}
