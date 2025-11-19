package com.projectmanagementapi.dto;

import java.util.List;

public record ProjectResponseDto(
        Long id,
        String name,
        String description,
        List<TaskResponseDto> tasks
) { }
