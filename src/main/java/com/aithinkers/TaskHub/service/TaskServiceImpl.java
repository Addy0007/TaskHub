package com.aithinkers.TaskHub.service;

import com.aithinkers.TaskHub.entity.Task;
import com.aithinkers.TaskHub.repository.TaskRepository;
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
