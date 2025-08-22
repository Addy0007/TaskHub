package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProjectService {

        private final ProjectRepository projectRepository;

        public List<Project> getAllProjects() {
            return projectRepository.findAll();
        }

        public Project getProjectById(Long id) {
            return projectRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
        }

        //Only ADMIN Can Create the Project
    //here we used preauthorize in here spring security will automatically checks the logged-in Users role before
    // executing createProject()
        @PreAuthorize("hasRole('ADMIN')")
        public Project createProject(Project project) {
            return projectRepository.save(project);
        }

}
