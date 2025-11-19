package com.projectmanagementapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequestDto(

        @NotBlank
        @Size(max = 100, message = "Project name must be at most 100 characters")
        String name,

        @NotBlank(message = "Project description cannot be empty")
        @Size(max = 500, message = "Project description must be at most 500 characters")
        String description

) {
}