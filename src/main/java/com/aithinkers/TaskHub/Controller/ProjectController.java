package com.aithinkers.TaskHub.Controller;

import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Enum.ProjectType;
import com.aithinkers.TaskHub.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Show all projects
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/list";
    }

    // Show create project form
    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("form", new Project());
        model.addAttribute("types", ProjectType.values());
        return "projects/create";
    }

    // Handle form submit
    @PostMapping("/new")
    public String createProject(@ModelAttribute("form") Project project) {
        projectService.createProject(project);
        return "redirect:/ui/projects";
    }

    // Project detail page
    @GetMapping("/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "projects/detail";

    }
}