package com.projectmanagementapi.service;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.mapper.TaskMapper;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.model.TaskStatus;
import com.projectmanagementapi.repository.ProjectRepository;
import com.projectmanagementapi.repository.TaskRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskDto createTask(Long projectId, Task task) {
        log.info("Creating task '{}' under project {}", task.getTitle(), projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.warn("Cannot create task: project {} not found", projectId);
                    return new ResourceNotFoundException("Project not found with id " + projectId);
                });

        task.setProject(project);

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
            log.info("Task '{}' status set to default '{}'", task.getTitle(), TaskStatus.TODO);
        }

        Task saved = taskRepository.save(task);
        log.info("Task '{}' created successfully with id {}", saved.getTitle(), saved.getId());
        return TaskMapper.toDto(saved);
    }

    public TaskDto getTaskById(Long taskId) {
        log.info("Fetching task with id={}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task with id={} not found", taskId);
                    return new ResourceNotFoundException("Task not found");
                });

        log.info("Task with id={} retrieved successfully", taskId);
        return TaskMapper.toDto(task);
    }

    public PagedResponse<TaskDto> getTasksForProject(Long projectId, int page, int size) {
        log.info("Fetching tasks for project {} page={} size={}", projectId, page, size);
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id " + projectId);
        }

        Page<Task> tasksPage = taskRepository.findByProject_Id(
                projectId,
                PageRequest.of(page, size)
        );

        List<TaskDto> dtos = tasksPage.getContent()
                .stream()
                .map(TaskMapper::toDto)
                .toList();

        log.info("Fetched {} tasks for project {} (page {} of {})",
                dtos.size(),
                projectId,
                tasksPage.getNumber(),
                tasksPage.getTotalPages()
        );

        return new PagedResponse<>(
                dtos,
                tasksPage.getNumber(),
                tasksPage.getSize(),
                tasksPage.getTotalElements()
        );
    }

    public TaskDto updateTask(Long taskId, Task updated) {
        log.info("Updating task with id={}", taskId);

        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updated.getTitle());
                    task.setDescription(updated.getDescription());

                    if (updated.getStatus() != null) {
                        task.setStatus(updated.getStatus());
                        log.info("Task {} status updated to '{}'", taskId, updated.getStatus());
                    }

                    Task saved = taskRepository.save(task);
                    log.info("Task with id={} updated successfully", taskId);
                    return TaskMapper.toDto(saved);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
    }

    public void deleteTask(Long taskId) {
        log.info("Deleting task with id={}", taskId);

        if (!taskRepository.existsById(taskId)) {
            log.warn("Cannot delete task: id={} not found", taskId);
            throw new ResourceNotFoundException("Task not found with id " + taskId);
        }

        taskRepository.deleteById(taskId);
        log.info("Task with id={} deleted successfully", taskId);
    }
}