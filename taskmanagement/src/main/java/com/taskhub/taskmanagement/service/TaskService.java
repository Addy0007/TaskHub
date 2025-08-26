package com.taskhub.taskmanagement.service;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public Task createTask(Task task) {
        List<Task> existingTasks = taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                task.getTaskName(), task.getTaskDescription(), task.getProjectId(), task.getCategory());
        if (existingTasks.isEmpty()) {
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task with same name, description, project ID, and category already exists.");
        }

    }

    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getTaskId()).orElse(null);
        if (existingTask != null) {
            existingTask.setTaskName(task.getTaskName());
            existingTask.setTaskDescription(task.getTaskDescription());
            existingTask.setProjectId(task.getProjectId());
            existingTask.setAssignedTo(task.getAssignedTo());
            existingTask.setStatus(task.getStatus());
            existingTask.setPriority(task.getPriority());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setCategory(task.getCategory());
            return taskRepository.save(existingTask);
        } else {
            return null;
        }
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

}
