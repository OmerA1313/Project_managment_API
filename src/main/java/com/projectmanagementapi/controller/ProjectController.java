package com.projectmanagementapi.controller;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping // CREATE a new project with request body
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    public ProjectDto getProject(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping
    public PagedResponse<ProjectDto> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return projectService.getAllProjects(page, size);
    }

    @PutMapping("/{id}") // UPDATE project
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}") // Delete project by id
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
