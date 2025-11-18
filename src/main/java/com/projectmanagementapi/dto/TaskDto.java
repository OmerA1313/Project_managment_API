package com.projectmanagementapi.dto;

import com.projectmanagementapi.model.TaskStatus;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status
) { }