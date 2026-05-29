package com.bantaybaha.project.dto;

import com.bantaybaha.project.entity.ProjectStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 180, message = "Title must not exceed 180 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 5000, message = "Description must not exceed 5000 characters")
        String description,

        @NotBlank(message = "Contractor is required")
        @Size(max = 180, message = "Contractor must not exceed 180 characters")
        String contractor,

        @NotNull(message = "Budget is required")
        @PositiveOrZero(message = "Budget must be zero or greater")
        @Digits(integer = 14, fraction = 2, message = "Budget must fit 14 integer digits and 2 decimal places")
        BigDecimal budget,

        @NotNull(message = "Status is required")
        ProjectStatus status,

        @NotNull(message = "Latitude is required")
        @DecimalMin(value = "-90.0", message = "Latitude must be at least -90")
        @DecimalMax(value = "90.0", message = "Latitude must not exceed 90")
        BigDecimal latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(value = "-180.0", message = "Longitude must be at least -180")
        @DecimalMax(value = "180.0", message = "Longitude must not exceed 180")
        BigDecimal longitude,

        LocalDate startDate,

        LocalDate targetCompletionDate
) {
}
