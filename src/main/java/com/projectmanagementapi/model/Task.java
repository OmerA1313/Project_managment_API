package com.projectmanagementapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Task() {
    }
}
