package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Project;
import org.springframework.data.domain.*;
import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project createProject(Project project);


    List<Project> getForUser(Long userId);
}
