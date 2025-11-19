package com.projectmanagementapi.mapper;

import com.projectmanagementapi.dto.ProjectResponseDto;
import com.projectmanagementapi.model.Project;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectResponseDto toDto(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getTasks()
                        .stream()
                        .map(TaskMapper::toDto)
                        .collect(Collectors.toList())
        );
    }
}