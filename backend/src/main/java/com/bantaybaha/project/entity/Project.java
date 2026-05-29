package com.bantaybaha.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 180)
    private String contractor;

    @Column(nullable = false, precision = 16, scale = 2)
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_completion_date")
    private LocalDate targetCompletionDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Project() {
    }

    public Project(
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
        this.title = title;
        this.description = description;
        this.contractor = contractor;
        this.budget = budget;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.targetCompletionDate = targetCompletionDate;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = ProjectStatus.UNKNOWN;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContractor() {
        return contractor;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getTargetCompletionDate() {
        return targetCompletionDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void update(
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
        this.title = title;
        this.description = description;
        this.contractor = contractor;
        this.budget = budget;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.targetCompletionDate = targetCompletionDate;
    }
}
