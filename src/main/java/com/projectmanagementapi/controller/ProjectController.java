package com.projectmanagementapi.controller;
import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectRequestDto;
import com.projectmanagementapi.dto.ProjectResponseDto;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.service.ProjectService;
import jakarta.validation.Valid;
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
    // -------------------------------------------------------
    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@Valid @RequestBody ProjectRequestDto projectRequestDto) {
        ProjectResponseDto saved = projectService.createProject(new Project(projectRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Fetching a project by unique id
    // -------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    // Fetching all projects, paginated
    // -------------------------------------------------------
    @GetMapping
    public ResponseEntity<PagedResponse<ProjectResponseDto>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    //Updating an existing project
    // -------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDto projectRequestDto
    ) {
        ProjectResponseDto updated = projectService.updateProject(id, new Project(projectRequestDto));
        return ResponseEntity.ok(updated);
    }

    // Deleting a project by unique id
    // -------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}

