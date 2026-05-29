package com.bantaybaha.project.dto;

import com.bantaybaha.project.entity.ProjectStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectDetailResponse(
        Long id,
        String title,
        String description,
        String contractor,
        BigDecimal budget,
        ProjectStatus status,
        BigDecimal latitude,
        BigDecimal longitude,
        LocalDate startDate,
        LocalDate targetCompletionDate
) {
}
