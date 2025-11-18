package com.projectmanagementapi.service;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.mapper.ProjectMapper;
import com.projectmanagementapi.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.projectmanagementapi.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository; //TODO perhaps we should think about a more abstract implementation

    public ProjectService(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {

        return projectRepository.save(project);
    }

    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id "+ id));
        return ProjectMapper.toDto(project);
    }

    public PagedResponse<ProjectDto> getAllProjects(int page, int size) {
        Page<Project> projectsPage = projectRepository.findAll(PageRequest.of(page, size));

        List<ProjectDto> dtos = projectsPage.getContent()
                .stream()
                .map(ProjectMapper::toDto)
                .toList();

        return new PagedResponse<>(
                dtos,
                projectsPage.getNumber(),
                projectsPage.getSize(),
                projectsPage.getTotalElements()
        );
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(project -> {
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            return projectRepository.save(project);
        }).orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id " + id);
        }
        projectRepository.deleteById(id);
    }
}
