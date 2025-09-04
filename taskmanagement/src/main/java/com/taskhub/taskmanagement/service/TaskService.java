package com.taskhub.taskmanagement.service;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    public List<Task> getAllTasks() {
        try {
            return taskRepository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve tasks", ex);
        }
    }

    public Task getTaskById(Long taskId) {
        if (taskId == null) {
            throw new NullPointerException("Task ID is required");
        }
        try {
            return taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task not found with ID " + taskId));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve task", ex);
        }
    }

    public Task createTask(Task task) {
        if (task == null) {
            throw new NullPointerException("Task is required");
        }
        try {
            List<Task> existingTasks = taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                    task.getTaskName(), task.getTaskDescription(), task.getProjectId(), task.getCategory());
            if (!existingTasks.isEmpty()) {
                throw new RuntimeException("Task with same name, description, project ID, and category already exists.");
            }
            return taskRepository.save(task);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create task", ex);
        }
    }

    public Task updateTask(Task task) {
        if (task == null || task.getTaskId() == null) {
            throw new NullPointerException("Task ID is required");
        }
        try {
            Task existingTask = taskRepository.findById(task.getTaskId()).orElseThrow(() -> new NoSuchElementException("Task not found with ID " + task.getTaskId()));
            existingTask.setTaskName(task.getTaskName());
            existingTask.setTaskDescription(task.getTaskDescription());
            existingTask.setProjectId(task.getProjectId());
            existingTask.setAssignedTo(task.getAssignedTo());
            existingTask.setStatus(task.getStatus());
            existingTask.setPriority(task.getPriority());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setCategory(task.getCategory());
            return taskRepository.save(existingTask);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to update task", ex);
        }
    }

    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new NullPointerException("Task ID is required");
        }
        try {
            taskRepository.deleteById(taskId);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete task", ex);
        }
    }

    public List<Task> searchTasks(String query) {
        if (query == null || query.isEmpty()) {
            throw new NullPointerException("Search query is required");
        }
        try {
            return taskRepository.searchTasks(query);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to search tasks", ex);
        }
    }



}
