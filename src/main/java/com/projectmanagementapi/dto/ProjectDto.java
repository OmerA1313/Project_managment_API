package com.projectmanagementapi.dto;

import java.util.List;

public record ProjectDto(
        Long id,
        String name,
        String description,
        List<TaskDto> tasks
) { }
