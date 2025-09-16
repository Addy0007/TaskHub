package com.aithinkers.TaskHub.service;

import com.aithinkers.TaskHub.entity.Task;

import java.util.List;

public interface TaskService {
   List<Task> getTasksForProject(Long id);
   List<Task> getAllTasks();
}
