package com.taskhub.taskmanagement.service;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testGetAllTasks() {
        System.out.println("Running testGetAllTasks");

        // Arrange
        List<Task> tasks = new ArrayList<>();
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertEquals(tasks, result);
        System.out.println("testGetTaskById passed");

    }

    @Test
    public void testGetTaskById() {
        System.out.println("Running testGetTaskById");
// Arrange
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Task result = taskService.getTaskById(1L);

        // Assert
        assertEquals(task, result);
        System.out.println("testGetTaskById passed");

    }

    @Test
    public void testGetTaskByIdNotFound() {
        System.out.println("Running testGetTaskByIdNotFound");
// Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Task result = taskService.getTaskById(1L);

        // Assert
        assertNull(result);
        System.out.println("testGetTaskByIdNotFound passed");

    }

    @Test
    public void testCreateTask() {
        System.out.println("Running testCreateTask");
        // Arrange
        Task task = new Task();
        when(taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                task.getTaskName(), task.getTaskDescription(), task.getProjectId(), task.getCategory()))
                .thenReturn(new ArrayList<>());
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task result = taskService.createTask(task);

        // Assert
        assertEquals(task, result);
        System.out.println("testCreateTask passed");

    }

    @Test
    public void testCreateTaskAlreadyExists() {
        System.out.println("Running testCreateTaskAlreadyExists");
        // Arrange
        Task task = new Task();
        when(taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                task.getTaskName(), task.getTaskDescription(), task.getProjectId(), task.getCategory()))
                .thenReturn(List.of(task));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> taskService.createTask(task));
        System.out.println("testCreateTaskAlreadyExists passed");
    }

    @Test
    public void testUpdateTask() {
        System.out.println("Running testUpdateTask");
// Arrange
        Task task = new Task();
        task.setTaskId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task result = taskService.updateTask(task);

        // Assert
        assertEquals(task, result);
        System.out.println("testUpdateTask passed");

    }

    @Test
    public void testUpdateTaskNotFound() {
        System.out.println("Running testUpdateTaskNotFound");
        // Arrange
        Task task = new Task();
        task.setTaskId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Task result = taskService.updateTask(task);

        // Assert
        assertNull(result);
        System.out.println("testUpdateTaskNotFound passed");

    }

    @Test
    public void testDeleteTask() {
        System.out.println("Running testDeleteTask");
        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, Mockito.times(1)).deleteById(1L);
        System.out.println("testDeleteTask passed");
    }
}


