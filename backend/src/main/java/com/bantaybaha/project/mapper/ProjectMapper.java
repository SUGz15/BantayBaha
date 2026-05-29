package com.bantaybaha.project.mapper;

import com.bantaybaha.project.dto.ProjectDetailResponse;
import com.bantaybaha.project.dto.ProjectMarkerResponse;
import com.bantaybaha.project.entity.Project;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static ProjectMarkerResponse toMarker(Project project) {
        return new ProjectMarkerResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getLatitude(),
                project.getLongitude()
        );
    }

    public static ProjectDetailResponse toDetail(Project project) {
        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getContractor(),
                project.getBudget(),
                project.getStatus(),
                project.getLatitude(),
                project.getLongitude(),
                project.getStartDate(),
                project.getTargetCompletionDate()
        );
    }
}
