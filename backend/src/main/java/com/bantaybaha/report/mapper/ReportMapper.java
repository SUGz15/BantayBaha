package com.bantaybaha.report.mapper;

import com.bantaybaha.report.dto.ReportResponse;
import com.bantaybaha.report.entity.Report;

public final class ReportMapper {

    private ReportMapper() {
    }

    public static ReportResponse toResponse(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getProject().getId(),
                report.getUser().getId(),
                report.getUser().getFullName(),
                report.getStatus(),
                report.getVerificationStatus(),
                report.getDescription(),
                report.getImageUrl(),
                report.getLatitude(),
                report.getLongitude(),
                report.getVerified(),
                report.getModeratedBy() == null ? null : report.getModeratedBy().getId(),
                report.getModerationReason(),
                report.getModeratedAt(),
                report.getCreatedAt()
        );
    }
}
