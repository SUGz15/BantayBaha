package com.bantaybaha.project.service;

import com.bantaybaha.exception.ResourceNotFoundException;
import com.bantaybaha.project.dto.ProjectDetailResponse;
import com.bantaybaha.project.dto.ProjectMarkerResponse;
import com.bantaybaha.project.dto.ProjectRequest;
import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.mapper.ProjectMapper;
import com.bantaybaha.project.repository.ProjectRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List<ProjectMarkerResponse> getMapProjects(ProjectStatus status, String search) {
        return projectRepository.findMapProjects(status, normalizeSearch(search))
                .stream()
                .map(ProjectMapper::toMarker)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDetailResponse getProject(Long id) {
        return projectRepository.findById(id)
                .map(ProjectMapper::toDetail)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Transactional
    public ProjectDetailResponse createProject(ProjectRequest request) {
        Project project = new Project(
                request.title().trim(),
                request.description().trim(),
                request.contractor().trim(),
                request.budget(),
                request.status(),
                request.latitude(),
                request.longitude(),
                request.startDate(),
                request.targetCompletionDate()
        );

        return ProjectMapper.toDetail(projectRepository.save(project));
    }

    @Transactional
    public ProjectDetailResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.update(
                request.title().trim(),
                request.description().trim(),
                request.contractor().trim(),
                request.budget(),
                request.status(),
                request.latitude(),
                request.longitude(),
                request.startDate(),
                request.targetCompletionDate()
        );

        return ProjectMapper.toDetail(project);
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        return search.trim();
    }
}
