package com.aithinkers.TaskHub.service;

import com.aithinkers.TaskHub.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project createProject(Project project);


    List<Project> getForUser(Long userId);
}
