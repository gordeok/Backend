package com.gordeok.report.service;

import com.gordeok.report.dto.CreateReportRequestDto;
import com.gordeok.report.dto.CreateReportResponseDto;
import com.gordeok.report.dto.ReportListResponseDto;
import com.gordeok.report.entity.Report;
import com.gordeok.report.repository.ReportRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // ── MY-12: 사기 신고 접수 ──
    @Transactional
    public CreateReportResponseDto createReport(Long reporterId,
                                                CreateReportRequestDto request,
                                                String evidenceImageUrls) {
        // 본인 신고 방지
        if (reporterId.equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("자신을 신고할 수 없습니다.");
        }

        // 대상 유저 존재 확인
        User target = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Report report = Report.builder()
                .reporterId(reporterId)
                .targetUserId(request.getTargetUserId())
                .postId(request.getPostId())
                .reason(request.getReason())
                .content(request.getContent())
                .evidenceImages(evidenceImageUrls)
                .build();

        Report saved = reportRepository.save(report);

        // 신고 접수 후 대상 유저의 hasScamReport 플래그 업데이트
        target.markScamReport();
        userRepository.save(target);

        return new CreateReportResponseDto(
                saved.getId(),
                saved.getStatus(),
                saved.getCreatedAt(),
                "신고가 접수되었습니다."
        );
    }

    // ── MY-13: 관리자 신고 목록 조회 ──
    public Page<ReportListResponseDto> getReports(String status, Pageable pageable) {
        Page<Report> reports = (status == null || status.isBlank())
                ? reportRepository.findAllByOrderByCreatedAtDesc(pageable)
                : reportRepository.findByStatusOrderByCreatedAtDesc(status, pageable);

        return reports.map(report -> {
            String reporterNickname = userRepository.findById(report.getReporterId())
                    .map(User::getNickname).orElse("알 수 없음");
            String targetNickname = userRepository.findById(report.getTargetUserId())
                    .map(User::getNickname).orElse("알 수 없음");
            return new ReportListResponseDto(report, reporterNickname, targetNickname);
        });
    }
}
