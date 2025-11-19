package com.projectmanagementapi.service;

import com.projectmanagementapi.dto.PagedResponse;
import com.projectmanagementapi.dto.TaskDto;
import com.projectmanagementapi.exception.ResourceNotFoundException;
import com.projectmanagementapi.model.Project;
import com.projectmanagementapi.model.Task;
import com.projectmanagementapi.model.TaskStatus;
import com.projectmanagementapi.repository.ProjectRepository;
import com.projectmanagementapi.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Project project;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setId(1L);
        project.setName("Demo Project");
    }

    // -------------------------------------------------------
    // CREATE TASK
    // -------------------------------------------------------
    @Test
    void testCreateTask_Success() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Testing create");

        Task savedTask = new Task();
        savedTask.setId(99L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("Testing create");
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto created = taskService.createTask(1L, task);

        assertNotNull(created);
        assertEquals(99L, created.id());
        assertEquals(TaskStatus.TODO, created.status());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testCreateTask_ProjectNotFound() {
        Task task = new Task();
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.createTask(1L, task));
    }

    // -------------------------------------------------------
    // GET TASK
    // -------------------------------------------------------
    @Test
    void testGetTaskById_Success() {
        Task task = new Task();
        task.setId(5L);
        task.setTitle("Demo");
        task.setDescription("desc");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setProject(project);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        TaskDto dto = taskService.getTaskById(5L);

        assertEquals("Demo", dto.title());
        verify(taskRepository).findById(5L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.getTaskById(99L));
    }

    // -------------------------------------------------------
    // GET TASKS FOR PROJECT
    // -------------------------------------------------------
    @Test
    void testGetTasksForProject() {
        Task t1 = new Task();
        t1.setId(1L);
        t1.setProject(project);
        t1.setStatus(TaskStatus.TODO);

        when(projectRepository.existsById(1L)).thenReturn(true);
        Page<Task> mockPage = new PageImpl<>(List.of(t1), PageRequest.of(0, 10), 1);
        when(taskRepository.findByProject_Id(eq(1L), any(PageRequest.class)))
                .thenReturn(mockPage);

        PagedResponse<TaskDto> response = taskService.getTasksForProject(1L, 0, 10);

        assertEquals(1, response.getItems().size());
        assertEquals(1L, response.getItems().get(0).id());
    }

    // -------------------------------------------------------
    // UPDATE TASK
    // -------------------------------------------------------
    @Test
    void testUpdateTask_Success() {
        Task existing = new Task();
        existing.setId(10L);
        existing.setTitle("Old");
        existing.setDescription("Old desc");
        existing.setStatus(TaskStatus.TODO);

        Task updated = new Task();
        updated.setTitle("New");
        updated.setDescription("New desc");
        updated.setStatus(TaskStatus.DONE);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        TaskDto saved = taskService.updateTask(10L, updated);

        assertEquals("New", saved.title());
        assertEquals("New desc", saved.description());
        assertEquals(TaskStatus.DONE, saved.status());
    }

    @Test
    void testUpdateTask_NotFound() {
        Task updated = new Task();
        when(taskRepository.findById(55L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(55L, updated));
    }

    // -------------------------------------------------------
    // DELETE TASK
    // -------------------------------------------------------
    @Test
    void testDeleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.existsById(77L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> taskService.deleteTask(77L));
    }
}
