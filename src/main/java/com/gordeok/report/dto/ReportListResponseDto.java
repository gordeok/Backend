package com.gordeok.report.dto;

import com.gordeok.report.entity.Report;
import lombok.Getter;
import java.time.LocalDateTime;

// 관리자용 신고 목록
@Getter
public class ReportListResponseDto {

    private Long reportId;
    private Long reporterId;
    private String reporterNickname;
    private Long targetUserId;
    private String targetNickname;
    private String reason;
    private String status;
    private LocalDateTime createdAt;

    public ReportListResponseDto(Report report, String reporterNickname, String targetNickname) {
        this.reportId = report.getId();
        this.reporterId = report.getReporterId();
        this.reporterNickname = reporterNickname;
        this.targetUserId = report.getTargetUserId();
        this.targetNickname = targetNickname;
        this.reason = report.getReason();
        this.status = report.getStatus();
        this.createdAt = report.getCreatedAt();
    }
}
