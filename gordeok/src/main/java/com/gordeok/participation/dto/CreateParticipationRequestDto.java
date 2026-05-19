package com.gordeok.participation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateParticipationRequestDto {

    @NotBlank(message = "받으시는 분 이름은 필수입니다.")
    private String realName;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @NotBlank(message = "편의점 지점명은 필수입니다.")
    private String storeName;

    private String requestMessage;
}