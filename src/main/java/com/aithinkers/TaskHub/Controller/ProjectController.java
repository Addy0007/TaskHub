package com.aithinkers.TaskHub.Controller;

import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Entity.Task;
import com.aithinkers.TaskHub.Entity.User;
import com.aithinkers.TaskHub.Enum.ProjectType;
import com.aithinkers.TaskHub.Enum.Role;
import com.aithinkers.TaskHub.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    /* we are using private final here as Construction Injection
     * since Spring 4.3 if a class has only one constructor ,spring will autmatically use it for
     * dependency injection even without @Autowired
     * Here Lambok's @requriedArgsconstructor generates that constructor for us
     * autowired best demo projects and private final lomboks constructor is good for production  */
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final UserService userService;
    private final TaskService taskService;

    // Show all projects Only for admin
    //ListProjects: ADMIN -> all ,USER -> only Assigned Projects Can be viewed
    @GetMapping
    public String listProjects(@AuthenticationPrincipal UserDetails principal, Model model) {
        User me = userService.findByEmail(principal.getUsername());
        List<Project> projects = (me.getRole() == Role.ADMIN)
                ? projectService.getAllProjects()
                : projectService.getForUser(me.getId());
        model.addAttribute("myself", me);
        model.addAttribute("projects", projects);
        return "projects/list";
    }

    //CREATE FORM
    //Get -Shows the Create Project Form so you can click and move below mentioned post request
    //the renders the Html form in thymeleaf
    // Show create project form
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createProjectForm(Model model) {
        model.addAttribute("form", new Project());
        model.addAttribute("types", ProjectType.values());
        return "projects/create";
    }

    //only Admin can create the project
    // Post here Handles form submission
    //This Endpoint is used when the user fills form clicks CREATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createProject(@ModelAttribute("form") Project project,
                                 @AuthenticationPrincipal UserDetails principal)
    {
        if(principal != null){
            User creator = userService.findByEmail(principal.getUsername());
            project.setCreatedBy(creator);
        }
        projectService.createProject(project);
        return "redirect:/dashboard";
    }

    // Project detail page
    @GetMapping("/{id}")
    public String projectDetail(@PathVariable Long id, Model model,
                                @AuthenticationPrincipal UserDetails principal) {
        User me = userService.findByEmail(principal.getUsername());
        model.addAttribute("myself",me);
        model.addAttribute("project", projectService.getProjectById(id));
        return "projects/detail";
    }

    /*Here @PathVariable is used to extract a value from a placeholder in the URI path itself.
           Use case: Fetching a specific item by its unique ID .here used the id of project working on
     @RequestParam is used to extract data from the query string (the part of a URL after the ?)
      here it extracts String->email

    RedirectAttributes-> is a Spring MVC helper that lets you pass data during a redirect.
	Without it, after a redirect, all request-scoped attributes would be lost.
	With ra.addFlashAttribute(), you can pass success/error messages to the redirected page

	->Notify is just a flash message to pop up in screen used in front end*/

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/members")
    public String addMember(@PathVariable Long id,
                            @RequestParam String email,
                            RedirectAttributes ra) {
        try {
            projectMemberService.addMemberByEmail(id, email.trim());
            ra.addFlashAttribute("notify", "Added " + email + " to the project.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/projects/" + id;
    }

    /*Here @PathVariable is used to extract a value from a placeholder in the URI path itself.
          Use case: Fetching a specific item by its unique ID .here used the id of project working on
    @RequestParam is used to extract data from the query string (the part of a URL after the ?)
     here it extracts String->email

   RedirectAttributes-> is a Spring MVC helper that lets you pass data during a redirect.
   Without it, after a redirect, all request-scoped attributes would be lost.
   With ra.addFlashAttribute(), you can pass success/error messages to the redirected page

   ->Notify is just a flash message to pop up in screen used in front end

   {Id}->project you are working on
   members->people working on the project
   userId->Specific User Inside that Project*/

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/members/{userId}/remove")
    public String removeMember(@PathVariable Long id,
                               @PathVariable Long userId,
                               RedirectAttributes ra) {
        projectMemberService.removeMember(id, userId);
        ra.addFlashAttribute("notify", "User removed from project.");
        return "redirect:/projects/" + id;
    }

    /*
	->•	Pagination doesn’t work with fetch join on a collection.
Pageable → An interface that describes the pagination request (page number, page size, sort order)
     */


    @GetMapping("/{id}/tasks")
    public String viewTasksForProject(@PathVariable("id") Long projectId,Model model) {
        Project project = projectService.getProjectById(projectId);
        List<Task> tasks = taskService.getTasksForProject(projectId);

        model.addAttribute("project",project);
        model.addAttribute("tasks",tasks);
        return "tasks/list";
    }
}