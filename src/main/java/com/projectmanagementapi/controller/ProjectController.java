package com.projectmanagementapi.controller;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.mapper.ProjectMapper;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "CRUD operations for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // -------------------------------------------------------------------------
    // CREATE PROJECT
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Create a new project",
            description = "Creates a new project using the provided name and description."
    )
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Project created successfully",
                                content = @Content(schema = @Schema(implementation = ProjectDto.class))
            ),@ApiResponse(responseCode = "400", description = "Invalid project data")})

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Project object containing name and description",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))
            )
            @RequestBody Project project
    ) {
        ProjectDto saved = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // -------------------------------------------------------------------------
    // GET PROJECT BY ID
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Get a project by ID",
            description = "Fetch project details by providing a project ID.")
    @ApiResponses({@ApiResponse(
                    responseCode = "200", description = "Project retrieved",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Project not found")})
    //-------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(@Parameter(description = "ID of the project to retrieve", example = "1") @PathVariable Long id
    ) {
        ProjectDto dto = projectService.getProjectById(id);
        return ResponseEntity.ok(dto);
    }

    // -------------------------------------------------------------------------
    // GET ALL PROJECTS (PAGINATED)
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Get all projects (paginated)", description = "Returns a paginated list of all available projects."
    ) @ApiResponses({@ApiResponse(
                    responseCode = "200", description = "List retrieved",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    })
    //-------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<PagedResponse<ProjectDto>> getAllProjects(
            @Parameter(description = "Page number (0-based index)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResponse<ProjectDto> response = projectService.getAllProjects(page, size);
        return ResponseEntity.ok(response);
    }

    // -------------------------------------------------------------------------
    // UPDATE PROJECT
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Update a project",
            description = "Updates the fields of an existing project."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Project updated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    //-------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "ID of the project to update", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated project details",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))
            )
            @RequestBody Project project
    ) {
        Project updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(ProjectMapper.toDto(updated));
    }

    // -------------------------------------------------------------------------
    // DELETE PROJECT
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Delete a project",
            description = "Deletes the specified project by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    //-------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete", example = "1")
            @PathVariable Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
