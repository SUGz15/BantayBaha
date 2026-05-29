package com.bantaybaha.project.dto;

import com.bantaybaha.project.entity.ProjectStatus;
import java.math.BigDecimal;

public record ProjectMarkerResponse(
        Long id,
        String title,
        ProjectStatus status,
        BigDecimal latitude,
        BigDecimal longitude
) {
}
