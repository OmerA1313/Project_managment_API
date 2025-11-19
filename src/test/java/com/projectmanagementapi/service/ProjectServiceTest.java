package com.projectmanagementapi.service;

import com.projectmanagementapi.dto.ProjectDto;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.mapper.ProjectMapper;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    // ----------------------------------------------------------
    @Test
    void testCreateProject() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.save(project)).thenReturn(project);

        ProjectDto result = projectService.createProject(project);

        assertEquals("Test Project", result.name());
        verify(projectRepository, times(1)).save(project);
    }

    // ----------------------------------------------------------
    @Test
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Desc");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectDto dto = projectService.getProjectById(1L);

        assertEquals("Test Project", dto.name());
        verify(projectRepository).findById(1L);
    }

    // ----------------------------------------------------------
    @Test
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                projectService.getProjectById(100L));
    }

    // ----------------------------------------------------------
    @Test
    void testGetAllProjects() {
        Project p1 = new Project();
        p1.setName("A");

        Project p2 = new Project();
        p2.setName("B");

        Page<Project> page = new PageImpl<>(List.of(p1, p2));
        when(projectRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        var result = projectService.getAllProjects(0, 10);
        assertEquals(2, result.getItems().size());
    }

    // ----------------------------------------------------------
    @Test
    void testUpdateProjectSuccess() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old");

        Project updated = new Project();
        updated.setName("New Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1L, updated);

        assertEquals("New Name", result.getName());
    }

    // ----------------------------------------------------------
    @Test
    void testUpdateProjectNotFound() {
        Project updated = new Project();
        updated.setName("New Name");

        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                projectService.updateProject(10L, updated));
    }

    // ----------------------------------------------------------
    @Test
    void testDeleteProjectSuccess() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProjectNotFound() {
        when(projectRepository.existsById(5L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                projectService.deleteProject(5L));
    }
}
