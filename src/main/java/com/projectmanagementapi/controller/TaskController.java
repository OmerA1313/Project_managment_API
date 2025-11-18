package com.projectmanagementapi.controller;
import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // CREATE task under a specific project
    @PostMapping("/projects/{projectId}/tasks")
    public Task createTask(@PathVariable Long projectId,
                           @RequestBody Task task) {
        return taskService.createTask(projectId, task);
    }

    @GetMapping("/tasks/{taskId}")
    public TaskDto getTask(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public PagedResponse<TaskDto> getTasksForProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return taskService.getTasksForProject(projectId, page, size);
    }

    // UPDATE task by id
    @PutMapping("/tasks/{taskId}")
    public Task updateTask(@PathVariable Long taskId,
                           @RequestBody Task updatedTask) {
        return taskService.updateTask(taskId, updatedTask);
    }

    // DELETE task by id
    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
