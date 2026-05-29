package com.bantaybaha.project.controller;

import com.bantaybaha.project.dto.ProjectDetailResponse;
import com.bantaybaha.project.dto.ProjectRequest;
import com.bantaybaha.project.service.ProjectService;
import com.bantaybaha.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/projects")
public class ProjectAdminController {

    private final ProjectService projectService;

    public ProjectAdminController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    ResponseEntity<ApiResponse<ProjectDetailResponse>> createProject(@Valid @RequestBody ProjectRequest request) {
        ProjectDetailResponse project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created", project));
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<ProjectDetailResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request
    ) {
        ProjectDetailResponse project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated", project));
    }
}
