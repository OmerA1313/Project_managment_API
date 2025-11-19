package com.projectmanagementapi.dto;

import com.projectmanagementapi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequestDTo (

    @NotBlank
    @Size(max = 100, message = "Project name must be at most 100 characters")
    String title,

    @NotBlank(message = "Project description cannot be empty")
    @Size(max = 500, message = "Project description must be at most 500 characters")
    String description,

    TaskStatus status

) {}