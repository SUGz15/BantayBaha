package com.bantaybaha.project.controller;

import com.bantaybaha.project.dto.ProjectDetailResponse;
import com.bantaybaha.project.dto.ProjectMarkerResponse;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.service.ProjectService;
import com.bantaybaha.util.ApiResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<ProjectMarkerResponse>>> getMapProjects(
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String search
    ) {
        List<ProjectMarkerResponse> projects = projectService.getMapProjects(status, search);
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved", projects));
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ProjectDetailResponse>> getProject(@PathVariable Long id) {
        ProjectDetailResponse project = projectService.getProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project retrieved", project));
    }
}
