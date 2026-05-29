package com.bantaybaha.report.controller;

import com.bantaybaha.report.dto.CreateReportRequest;
import com.bantaybaha.report.dto.ReportResponse;
import com.bantaybaha.report.service.ReportService;
import com.bantaybaha.user.entity.User;
import com.bantaybaha.util.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/projects/{projectId}/reports")
    ResponseEntity<ApiResponse<ReportResponse>> submitReport(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateReportRequest request,
            @AuthenticationPrincipal User user
    ) {
        ReportResponse report = reportService.submitReport(projectId, request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Report submitted for review", report));
    }

    @GetMapping("/projects/{projectId}/reports")
    ResponseEntity<ApiResponse<List<ReportResponse>>> getProjectReports(@PathVariable Long projectId) {
        List<ReportResponse> reports = reportService.getProjectReports(projectId);
        return ResponseEntity.ok(ApiResponse.success("Project reports retrieved", reports));
    }

    @GetMapping("/reports/me")
    ResponseEntity<ApiResponse<List<ReportResponse>>> getMyReports(@AuthenticationPrincipal User user) {
        List<ReportResponse> reports = reportService.getMyReports(user);
        return ResponseEntity.ok(ApiResponse.success("User reports retrieved", reports));
    }
}
