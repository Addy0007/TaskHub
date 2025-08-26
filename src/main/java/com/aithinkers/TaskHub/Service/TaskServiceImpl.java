package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Task;
import com.aithinkers.TaskHub.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepo;

    @Override
    public List<Task> getTasksForProject(Long projectId) {
        return taskRepo.findAllByProjectIdWithAssignees(projectId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }
}
