package com.projectmanagementapi.service;

import com.projectmanagementapi.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.projectmanagementapi.repository.ProjectRepository;

@Service
public class ProjectService {

    private ProjectRepository projectRepository; //TODO perhaps we should think about a more abstract implementation

    public ProjectService(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {

        return projectRepository.save(project);
    }

    public Page<Project> getAllProjects(int page, int size) {
        return projectRepository.findAll(PageRequest.of(page, size));
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(project -> {
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            return projectRepository.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id " + id);
        }
        projectRepository.deleteById(id);
    }
}
