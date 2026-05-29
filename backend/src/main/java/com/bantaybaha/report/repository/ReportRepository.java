package com.bantaybaha.report.repository;

import com.bantaybaha.report.entity.Report;
import com.bantaybaha.report.entity.ReportStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByProjectIdOrderByCreatedAtDesc(Long projectId);

    List<Report> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Report> findByStatusOrderByCreatedAtDesc(ReportStatus status);
}
