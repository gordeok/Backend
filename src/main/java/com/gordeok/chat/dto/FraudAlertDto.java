package com.gordeok.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FraudAlertDto {

    private String messageType;  // FRAUD_WARNING | FRAUD_DANGER

    private String reason;
}
