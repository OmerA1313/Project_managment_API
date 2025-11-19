
package com.projectmanagementapi.service;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectRequestDto;
import com.projectmanagementapi.dto.ProjectResponseDto;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.mapper.ProjectMapper;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectResponseDto createProject(Project project) {
        log.info("Creating new project with name='{}'", project.getName());
        ProjectResponseDto saved = ProjectMapper.toDto(projectRepository.save(project));
        log.info("Project created successfully with id={}", saved.id());
        return saved;
    }

    public ProjectResponseDto getProjectById(Long id) {
        log.info("Fetching project with id={}", id);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));

        log.info("Project with id={} retrieved successfully", id);
        return ProjectMapper.toDto(project);
    }

    public PagedResponse<ProjectResponseDto> getAllProjects(int page, int size) {
        log.info("Fetching all projects page={} size={}", page, size);

        Page<Project> projectsPage = projectRepository.findAll(PageRequest.of(page, size));

        List<ProjectResponseDto> dtos = projectsPage.getContent()
                .stream()
                .map(ProjectMapper::toDto)
                .toList();

        log.info("Fetched {} projects (page {} of {})",
                dtos.size(),
                projectsPage.getNumber(),
                projectsPage.getTotalPages()
        );

        return new PagedResponse<>(
                dtos,
                projectsPage.getNumber(),
                projectsPage.getSize(),
                projectsPage.getTotalElements()
        );
    }

    public ProjectResponseDto updateProject(Long id, Project updatedProject) {
        log.info("Updating project with id={}", id);

        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(updatedProject.getName());
                    project.setDescription(updatedProject.getDescription());
                    Project saved = projectRepository.save(project);
                    log.info("Project with id={} updated successfully", id);
                    return ProjectMapper.toDto(saved);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot update project: id={} not found", id);
                    return new ResourceNotFoundException("Project not found with id " + id);
                });
    }

    public void deleteProject(Long id) {
        log.info("Deleting project with id={}", id);

        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id " + id);
        }

        projectRepository.deleteById(id);
        log.info("Project with id={} deleted successfully", id);
    }
}