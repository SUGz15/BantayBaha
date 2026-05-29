package com.bantaybaha.report.service;

import com.bantaybaha.exception.ResourceNotFoundException;
import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.repository.ProjectRepository;
import com.bantaybaha.report.dto.CreateReportRequest;
import com.bantaybaha.report.dto.ModerateReportRequest;
import com.bantaybaha.report.dto.ReportResponse;
import com.bantaybaha.report.entity.Report;
import com.bantaybaha.report.entity.ReportStatus;
import com.bantaybaha.report.mapper.ReportMapper;
import com.bantaybaha.report.repository.ReportRepository;
import com.bantaybaha.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProjectRepository projectRepository;

    public ReportService(ReportRepository reportRepository, ProjectRepository projectRepository) {
        this.reportRepository = reportRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ReportResponse submitReport(Long projectId, CreateReportRequest request, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Report report = new Report(
                project,
                user,
                request.verificationStatus(),
                request.description().trim(),
                normalizeImageUrl(request.imageUrl()),
                request.latitude(),
                request.longitude()
        );

        return ReportMapper.toResponse(reportRepository.save(report));
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getProjectReports(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found");
        }

        return reportRepository.findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(ReportMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getMyReports(User user) {
        return reportRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(ReportMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getPendingReports() {
        return reportRepository.findByStatusOrderByCreatedAtDesc(ReportStatus.PENDING)
                .stream()
                .map(ReportMapper::toResponse)
                .toList();
    }

    @Transactional
    public ReportResponse approveReport(Long reportId, ModerateReportRequest request, User moderator) {
        Report report = findReport(reportId);
        report.approve(moderator, normalizeReason(request.reason()));
        return ReportMapper.toResponse(report);
    }

    @Transactional
    public ReportResponse rejectReport(Long reportId, ModerateReportRequest request, User moderator) {
        Report report = findReport(reportId);
        report.reject(moderator, normalizeReason(request.reason()));
        return ReportMapper.toResponse(report);
    }

    private String normalizeImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        return imageUrl.trim();
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
    }

    private String normalizeReason(String reason) {
        if (reason == null || reason.isBlank()) {
            return null;
        }

        return reason.trim();
    }
}
