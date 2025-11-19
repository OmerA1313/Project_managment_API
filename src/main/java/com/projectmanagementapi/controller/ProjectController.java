package com.projectmanagementapi.controller;
import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Creating a new project
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody Project project) {
        ProjectDto saved = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Fetching a project by unique id
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // Fetching all projects, paginated
    @GetMapping
    public ResponseEntity<PagedResponse<ProjectDto>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    //Updating an existing project
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @RequestBody Project project
    ) {
        ProjectDto updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(updated);
    }

    // Deleting a project by unique id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}

