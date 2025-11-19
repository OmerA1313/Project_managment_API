package com.projectmanagementapi.mapper;

import com.projectmanagementapi.dto.TaskResponseDto;
import com.projectmanagementapi.model.Task;

public class TaskMapper {

    public static TaskResponseDto toDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }
}