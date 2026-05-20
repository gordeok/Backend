package com.gordeok.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateParticipationResponseDto {

    private Long participationId;

    private Long chatRoomId;

    private String memberStatus;

    private String message;
}