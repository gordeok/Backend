package com.gordeok.report.controller;

import com.gordeok.global.storage.FileStorageService;
import com.gordeok.report.dto.CreateReportRequestDto;
import com.gordeok.report.dto.CreateReportResponseDto;
import com.gordeok.report.dto.ReportListResponseDto;
import com.gordeok.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final FileStorageService fileStorageService;

    // ── MY-12: 사기 신고 접수 ──
    // POST /api/reports  (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateReportResponseDto> createReport(
            @RequestParam Long reporterId,
            @RequestPart("data") @Valid CreateReportRequestDto request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // 이미지 로컬 저장 후 URL 목록 생성 (최대 5장)
        String evidenceUrls = null;
        if (images != null && !images.isEmpty()) {
            List<String> urls = images.stream()
                    .limit(5)
                    .map(img -> fileStorageService.store(img, "reports"))
                    .collect(Collectors.toList());
            evidenceUrls = String.join(",", urls);
        }

        return ResponseEntity.ok(reportService.createReport(reporterId, request, evidenceUrls));
    }

    // ── MY-13: 신고 목록 조회 (관리자용, P2) ──
    @GetMapping
    public ResponseEntity<Page<ReportListResponseDto>> getReports(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reportService.getReports(status, pageable));
    }
}
