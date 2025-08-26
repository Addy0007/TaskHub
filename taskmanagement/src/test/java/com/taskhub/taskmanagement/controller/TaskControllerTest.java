package com.taskhub.taskmanagement.controller;

import com.taskhub.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.entity.TaskCategory;
import com.taskhub.taskmanagement.service.TaskService;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Model model;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void testGetAllTasks() {
        // Arrange
        System.out.println("Running testGetAllTasks");

        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTaskName("Test Task");
        tasks.add(task);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act
        String viewName = taskController.getAllTasks(model);

        // Assert
        assertEquals("tasks", viewName);
        verify(model, times(1)).addAttribute("tasks", tasks);
    }

    @Test
    public void testGetTaskById() {
        // Arrange
        System.out.println("Running testGetTaskById");
        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
        when(taskService.getTaskById(1L)).thenReturn(task);

        // Act
        String viewName = taskController.getTaskById(1L, model);

        // Assert
        assertEquals("task", viewName);
        verify(model, times(1)).addAttribute("task", task);
    }

    @Test
    public void testCreateTaskForm() {
        // Act
        System.out.println("Running testCreateTaskForm");

        String viewName = taskController.createTaskForm(model);

        // Assert
        assertEquals("create-task", viewName);
        verify(model, times(1)).addAttribute(eq("task"), any(Task.class));
    }

    @Test
    public void testCreateTask() {
        // Arrange
        System.out.println("Running testCreateTask");
        // Arrange
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);
        when(taskService.createTask(task)).thenReturn(task);

        // Act
        String viewName = taskController.createTask(task, model);


        // Assert
        assertEquals("redirect:/tasks", viewName);


    }
    @Test
    public void testCreateTaskWithInvalidData() {
        // Arrange
        Task task = new Task();
        task.setTaskName(null);
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);

        // Act
        String viewName = taskController.createTask(task, model);

        // Assert
        assertEquals("redirect:/tasks", viewName);
    }



    @Test
    public void testCreateTaskWithException() {
        // Arrange
        System.out.println("Running testCreateTaskWithException");

        // Arrange
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);
        when(taskService.createTask(any(Task.class))).thenThrow(new RuntimeException("Task with same name, description, project ID, and category already exists."));

        // Act
        String viewName = taskController.createTask(task, model);

        // Assert
        assertEquals("create-task", viewName);
        verify(model, times(1)).addAttribute("errorMessage", "Task with same name, description, project ID, and category already exists.");

    }

    @Test
    public void testUpdateTaskForm() {
        // Arrange
        System.out.println("Running testUpdateTaskForm");

        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
        when(taskService.getTaskById(1L)).thenReturn(task);

        // Act
        String viewName = taskController.updateTaskForm(1L, model);

        // Assert
        assertEquals("update-task", viewName);
        verify(model, times(1)).addAttribute("task", task);
    }

    @Test
    public void testUpdateTask() {
        // Arrange
        System.out.println("Running testUpdateTask");

        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);
        when(taskService.updateTask(any(Task.class))).thenReturn(task);

        // Act
        String viewName = taskController.updateTask(1L, task,model);

        // Assert
        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).updateTask(any(Task.class));
    }
    @Test
    public void testUpdateTaskWithInvalidData() {
        // Arrange
        Task task = new Task();
        task.setTaskId(1L);
        task.setTaskName(null);
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);
//act
        String viewName = taskController.updateTask(1L, task, model);
        // Assert
        assertEquals("redirect:/tasks", viewName); // Expect "redirect:/tasks" view name
        verify(model, never()).addAttribute(eq("errorMessage"), anyString()); // Verify that addAttribute is never called

    }


    @Test
    public void testDeleteTask() {
        // Act
        System.out.println("Running testDeleteTask");

        String viewName = taskController.deleteTask(1L);

        // Assert
        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    public void testGetAllTasksEmptyList() {
        // Arrange
        System.out.println("Running testGetAllTasksEmptyList");

        List<Task> tasks = new ArrayList<>();
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act
        String viewName = taskController.getAllTasks(model);

        // Assert
        assertEquals("tasks", viewName);
        verify(model, times(1)).addAttribute("tasks", tasks);
    }

    @Test
    public void testGetTaskByIdNullTask() {
        // Arrange
        System.out.println("Running testGetTaskByIdNullTask");

        when(taskService.getTaskById(1L)).thenReturn(null);

        // Act
        String viewName = taskController.getTaskById(1L, model);

        // Assert
        assertEquals("task", viewName);
        verify(model, times(1)).addAttribute("task", null);
    }

    @Test
    public void testCreateTaskWithNullTask() {
        // Act
        System.out.println("Running testCreateTaskWithNullTask");
        String viewName = taskController.createTask(null, model);
        // Assert
        assertEquals("create-task", viewName); // Expect "create-task" view name
        verify(model, times(1)).addAttribute(eq("errorMessage"), anyString());
    }


    @Test
    public void testUpdateTaskFormWithNullTask() {
        // Arrange
        System.out.println("Running testUpdateTaskFormWithNullTask");
        when(taskService.getTaskById(1L)).thenReturn(null);

        // Act
        String viewName = taskController.updateTaskForm(1L, model);

        // Assert
        assertEquals("update-task", viewName);
        verify(model, times(1)).addAttribute("task", null);
    }

    @Test
    public void testUpdateTaskWithNullTask() {
        // Act
        System.out.println("Running testUpdateTaskWithNullTask");
        String viewName = taskController.updateTask(1L, null,model);

        // Assert
        assertEquals("redirect:/tasks", viewName); // This will fail because the method doesn't handle null tasks
    }

    @Test
    public void testDeleteTaskWithInvalidId() {
        // Act
        System.out.println("Running testDeleteTaskWithInvalidId");
        String viewName = taskController.deleteTask(null);

        // Assert
        assertEquals("redirect:/tasks", viewName); // This might fail if the service method doesn't handle null IDs
        verify(taskService, times(0)).deleteTask(anyLong());
    }



}
