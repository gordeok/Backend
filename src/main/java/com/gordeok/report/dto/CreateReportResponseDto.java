package com.gordeok.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateReportResponseDto {
    private Long reportId;
    private String status;
    private LocalDateTime createdAt;
    private String message;
}
