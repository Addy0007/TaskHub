package com.aithinkers.TaskHub.controller;

import com.aithinkers.TaskHub.entity.Project;
import com.aithinkers.TaskHub.entity.Task;
import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.Enum.ProjectType;
import com.aithinkers.TaskHub.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public String listProjects(@AuthenticationPrincipal UserDetails principal, Model model,
                               @RequestParam (value = "jwt_token", required = false) String jwtToken) {

        // 1) Check role from Spring Security authorities
        boolean isAdmin = principal.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        // 2) Load the current user entity (we still need the id)
        User me = userService.findByName(principal.getUsername());

        // 3) Pick the correct project list
        List<Project> projects = isAdmin
                ? projectService.getAllProjects()
                : projectService.getForUser(me.getId());

        // 4) Render
        model.addAttribute("myself", me);
        model.addAttribute("projects", projects);
        model.addAttribute("jwt", jwtToken);
        return "projects/list";
    }

    //CREATE FORM
    //Get -Shows the Create Project Form so you can click and move below mentioned post request
    //the renders the Html form in thymeleaf
    // Show create project form
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String createProjectForm(Model model,
                                    @RequestParam(value = "jwt_token",required = false) String jwt) {
        model.addAttribute("form", new Project());
        model.addAttribute("types", ProjectType.values());
        model.addAttribute("jwt",jwt);
        return "projects/create";
    }

    //only Admin can create the project
    // Post here Handles form submission
    //This Endpoint is used when the user fills form clicks CREATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String createProject(@ModelAttribute("form") Project project,
                                 @AuthenticationPrincipal UserDetails principal,
                                @RequestParam("jwt_token") String jwt)
    {
        if(principal != null){
            User creator = userService.findByName(principal.getUsername());
            project.setCreatedBy(creator);
        }
        projectService.createProject(project);

        String encoded = UriUtils.encode(jwt , StandardCharsets.UTF_8);
        return "redirect:/projects/dashboard?jwt_token="+ encoded;
    }

    // Project detail page
    @GetMapping("/{id:\\d+}")
    public String projectDetail(@PathVariable Long id, Model model,
                                @RequestParam(value = "jwt_token",required = false) String jwtToken,
                                @AuthenticationPrincipal UserDetails principal) {
        User me = userService.findByName(principal.getUsername());
        Project project = projectService.getProjectById(id);
        boolean isAdmin = me != null && "ROLE_ADMIN".equalsIgnoreCase(me.getRole());// << reliable check

        model.addAttribute("myself",me);
        model.addAttribute("project", project);
        model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("jwt", jwtToken == null ? "" : jwtToken);
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
    @PostMapping("/{id:\\d+}/members")
    public String addMember(@PathVariable Long id,
                            @RequestParam String email,
                            @RequestParam(value = "jwt_token",required = false) String jwtToken,
                            RedirectAttributes ra,
                            @AuthenticationPrincipal UserDetails principal) {
        try {
            projectMemberService.addMemberByEmail(id, email.trim());
            ra.addFlashAttribute("notify", "Added " + email + " to the project.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }

        if (jwtToken != null && !jwtToken.isBlank()) {
            ra.addAttribute("jwt_token", URLEncoder.encode(jwtToken, StandardCharsets.UTF_8));
        }

        return "redirect:/projects/{id}" ;
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
    @PostMapping("/{id:\\d+}/members/{userId}/remove")
    public String removeMember(@PathVariable Long id,
                               @PathVariable Long userId,
                               @RequestParam(value = "jwt_token",required = false) String jwtToken,
                               RedirectAttributes ra) {
        projectMemberService.removeMember(id, userId);
        ra.addFlashAttribute("notify", "User removed from project.");

        if (jwtToken != null && !jwtToken.isBlank()) {
            ra.addAttribute("jwt_token", URLEncoder.encode(jwtToken, StandardCharsets.UTF_8));
        }
        return "redirect:/projects/{id}" ;
    }

    /*private static String appendJwt(String jwt){
        return (jwt == null || jwt.isBlank()) ? "" :
                "?jwt_token=" + java.net.URLEncoder.encode(jwt, StandardCharsets.UTF_8);
    }*/

    /*
	->•	Pagination doesn’t work with fetch join on a collection.
Pageable → An interface that describes the pagination request (page number, page size, sort order)
     */


    @GetMapping("/{id:\\d+}/tasks")
    public String viewTasksForProject(@PathVariable("id") Long projectId,
                                      @AuthenticationPrincipal UserDetails me,
            @RequestParam(value = "jwt_token", required = false) String jwt,Model model) {
        Project project = projectService.getProjectById(projectId);
        List<Task> tasks = taskService.getTasksForProject(projectId);

        model.addAttribute("project",project);
        model.addAttribute("tasks",tasks);
        model.addAttribute("me",me);
        model.addAttribute("jwt",jwt);
        return "tasks/list";
    }
}