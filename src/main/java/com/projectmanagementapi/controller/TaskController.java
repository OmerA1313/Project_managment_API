package com.projectmanagementapi.controller;
import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.service.TaskService;
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
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskDto> createTask(
            @PathVariable Long projectId,
            @RequestBody Task task
    ) {
        TaskDto saved = taskService.createTask(projectId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Fetch task by unique id
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // Fetch all tasks under project specified by id
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<PagedResponse<TaskDto>> getTasksForProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(taskService.getTasksForProject(projectId, page, size));
    }

    // Update existing task specified by id
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long taskId,
            @RequestBody Task updatedTask
    ) {
        TaskDto updated = taskService.updateTask(taskId, updatedTask);
        return ResponseEntity.ok(updated);
    }

    // Delete task specified by id
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
}
