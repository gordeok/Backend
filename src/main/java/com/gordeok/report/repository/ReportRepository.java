package com.gordeok.report.repository;

import com.gordeok.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByTargetUserId(Long targetUserId);

    Page<Report> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    Page<Report> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByReporterIdAndTargetUserId(Long reporterId, Long targetUserId);
}
