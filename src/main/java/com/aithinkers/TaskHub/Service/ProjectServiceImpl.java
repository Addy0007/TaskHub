package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    //Only ADMIN Can Create the Project
    //here we used preauthorize in here spring security will automatically checks the logged-in Users role before
    // executing createProject()
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getForUser(Long userId) {
        return projectRepository.findAllByMemberId(userId);
    }

    /* Pagination is useful when you expect a user to have many projects.
->•	Pagination doesn’t work with fetch join on a collection.
Pageable → An interface that describes the pagination request (page number, page size, sort order)*/



}

