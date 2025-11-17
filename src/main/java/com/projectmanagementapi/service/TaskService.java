package com.projectmanagementapi.service;

import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.model.TaskStatus;
import com.projectmanagementapi.repository.ProjectRepository;
import com.projectmanagementapi.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));

        task.setProject(project);
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        return taskRepository.save(task);
    }

    public Page<Task> getTasksForProject(Long projectId, int page, int size) {
        return taskRepository.findByProject_Id(projectId, PageRequest.of(page, size));
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
    }

    public Task updateTask(Long taskId, Task updated) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updated.getTitle());
                    task.setDescription(updated.getDescription());
                    if (updated.getStatus() != null) {
                        task.setStatus(updated.getStatus());
                    }
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}