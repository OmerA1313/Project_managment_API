package com.projectmanagementapi.controller;
import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskRequestDTo;
import com.projectmanagementapi.dto.TaskResponseDto;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create a new task under project specified by id
    // -------------------------------------------------------
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponseDto> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequestDTo taskRequestDto
    ) {
        TaskResponseDto saved = taskService.createTask(projectId, new Task(taskRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Fetch task by unique id
    // -------------------------------------------------------
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // Fetch all tasks under project specified by id
    // -------------------------------------------------------
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<PagedResponse<TaskResponseDto>> getTasksForProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId, page, size));
    }

    // Update existing task specified by id
    // -------------------------------------------------------
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskRequestDTo updatedTaskRequestDto
    ) {
        TaskResponseDto updated = taskService.updateTask(taskId, new Task(updatedTaskRequestDto));
        return ResponseEntity.ok(updated);
    }

    // Delete task specified by id
    // -------------------------------------------------------
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
}
