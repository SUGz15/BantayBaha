package com.bantaybaha.report.dto;

import jakarta.validation.constraints.Size;

public record ModerateReportRequest(
        @Size(max = 500, message = "Reason must not exceed 500 characters")
        String reason
) {
}
