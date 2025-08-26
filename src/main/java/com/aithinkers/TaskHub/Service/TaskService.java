package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Task;

import java.util.List;

public interface TaskService {
   List<Task> getTasksForProject(Long id);

   List<Task> getAllTasks();
}
