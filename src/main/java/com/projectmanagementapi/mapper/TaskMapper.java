package com.projectmanagementapi.mapper;

import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.model.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}