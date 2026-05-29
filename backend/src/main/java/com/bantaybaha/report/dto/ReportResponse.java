package com.bantaybaha.report.dto;

import com.bantaybaha.report.entity.ReportStatus;
import com.bantaybaha.report.entity.VerificationStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record ReportResponse(
        Long id,
        Long projectId,
        Long userId,
        String reporterName,
        ReportStatus status,
        VerificationStatus verificationStatus,
        String description,
        String imageUrl,
        BigDecimal latitude,
        BigDecimal longitude,
        Boolean verified,
        Long moderatedById,
        String moderationReason,
        Instant moderatedAt,
        Instant createdAt
) {
}
