package com.projectmanagementapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projectmanagementapi.dto.TaskRequestDTo;
import jakarta.persistence.*;

@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    public Task() {
    }

    public Task(TaskRequestDTo taskRequestDTo){
        this.title = taskRequestDTo.title();
        this.description = taskRequestDTo.description();
        this.taskStatus = taskRequestDTo.status();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public Project getProject() {
        return project;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
