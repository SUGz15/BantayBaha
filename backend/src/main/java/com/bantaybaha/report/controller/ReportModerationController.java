package com.bantaybaha.report.controller;

import com.bantaybaha.report.dto.ModerateReportRequest;
import com.bantaybaha.report.dto.ReportResponse;
import com.bantaybaha.report.service.ReportService;
import com.bantaybaha.user.entity.User;
import com.bantaybaha.util.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportModerationController {

    private final ReportService reportService;

    public ReportModerationController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/pending")
    ResponseEntity<ApiResponse<List<ReportResponse>>> getPendingReports() {
        List<ReportResponse> reports = reportService.getPendingReports();
        return ResponseEntity.ok(ApiResponse.success("Pending reports retrieved", reports));
    }

    @PostMapping("/{reportId}/approve")
    ResponseEntity<ApiResponse<ReportResponse>> approveReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ModerateReportRequest request,
            @AuthenticationPrincipal User moderator
    ) {
        ReportResponse report = reportService.approveReport(reportId, request, moderator);
        return ResponseEntity.ok(ApiResponse.success("Report approved", report));
    }

    @PostMapping("/{reportId}/reject")
    ResponseEntity<ApiResponse<ReportResponse>> rejectReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ModerateReportRequest request,
            @AuthenticationPrincipal User moderator
    ) {
        ReportResponse report = reportService.rejectReport(reportId, request, moderator);
        return ResponseEntity.ok(ApiResponse.success("Report rejected", report));
    }
}
