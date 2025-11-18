package com.projectmanagementapi.controller;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.mapper.TaskMapper;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.service.TaskService;

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
@RequestMapping
@Tag(name = "Tasks", description = "CRUD operations for managing tasks under projects")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // -------------------------------------------------------------------------
    // CREATE TASK UNDER PROJECT
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Create a new task",
            description = "Creates a task and assigns it to a project."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "400", description = "Invalid task format")
    })
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(
            @Parameter(description = "ID of the project to attach the task to", example = "1")
            @PathVariable Long projectId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task payload: title, description, and optional status",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Task.class))
            )
            @RequestBody Task task
    ) {
        Task saved = taskService.createTask(projectId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.toDto(saved));
    }

    // -------------------------------------------------------------------------
    // GET TASK BY ID
    // -------------------------------------------------------------------------
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task retrieved",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/tasks/{taskId}")
    public TaskDto getTask(
            @Parameter(description = "ID of the task to retrieve", example = "10")
            @PathVariable Long taskId
    ) {
        return taskService.getTaskById(taskId); // already returns DTO
    }

    // -------------------------------------------------------------------------
    // GET TASKS FOR PROJECT (PAGINATED)
    // -------------------------------------------------------------------------
    @Operation(
            summary = "Get tasks for a project (paginated)",
            description = "Returns a paginated list of tasks belonging to the given project."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/projects/{projectId}/tasks")
    public PagedResponse<TaskDto> getTasksForProject(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long projectId,

            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return taskService.getTasksForProject(projectId, page, size);
    }

    // -------------------------------------------------------------------------
    // UPDATE TASK
    // -------------------------------------------------------------------------
    @Operation(summary = "Update an existing task")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/tasks/{taskId}")
    public TaskDto updateTask(
            @Parameter(description = "ID of the task to update", example = "5")
            @PathVariable Long taskId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New task fields",
                    content = @Content(schema = @Schema(implementation = TaskDto.class))
            )
            @RequestBody Task updatedTask
    ) {
        Task updated = taskService.updateTask(taskId, updatedTask);
        return TaskMapper.toDto(updated);
    }

    // -------------------------------------------------------------------------
    // DELETE TASK
    // -------------------------------------------------------------------------
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", example = "7")
            @PathVariable Long taskId
    ) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
}
