package com.projectmanagementapi.mapper;

import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.model.Project;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectDto toDto(Project project) {
        return new ProjectDto(
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