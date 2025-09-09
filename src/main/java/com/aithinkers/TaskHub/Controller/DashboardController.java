package com.aithinkers.TaskHub.Controller;

import com.aithinkers.TaskHub.entity.Project;
import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.service.ProjectService;
import com.aithinkers.TaskHub.service.TaskService;
import com.aithinkers.TaskHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    /* we are using private final here as Construction Injection
    * since Spring 4.3 if a class has only one constructor ,spring will autmatically use it for
    * dependency injection even without @Autowired
    * Here Lambok's @requriedArgsconstructor generates that constructor for us
    * autowired best demo projects and private final lomboks constructor is good for production  */

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

/*  @Authenticated Principal UserDetails Principal
* Here Spring Security Injects the Logged-in User or Person as Principal
* if no one logged in then its an NULL
* if someone Logged in ->it contains their Username + roles
*
* And as we Know Model was an DataBag passes data from controller to view TEMPLATE
*
* In This method IF->User Logged In
* Get their username (usually their email) from principal.
 Use userService.findByEmail() to fetch the full User entity from the database.
 * 	Add it to the model with key "myself".
Purpose: in the dashboard page, you’ll be able to display info about the currently logged-in user (
* like “Welcome, Sandeep!” or show a Create Project button if user has the right role).
* Similarly the Projects it fetches all the projects */


    @GetMapping("/projects/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model,
                            @RequestParam(value = "jwt_token", required = false)String jwtToken) {

        // get the logged-in user entity (still needed for id, email, etc.)
        User me = userService.findByName(principal.getUsername());

        // check if user has ROLE_ADMIN
        boolean isAdmin = principal.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .anyMatch("ROLE_ADMIN" :: equals);

        // load projects accordingly
        List<Project> projects = isAdmin
                ? projectService.getAllProjects()
                : projectService.getForUser(me.getId());

        model.addAttribute("myself", me);
        model.addAttribute("projects", projects);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("tasks", taskService.getAllTasks());
        // whatever data you already add, plus:
        if (jwtToken != null && !jwtToken.isBlank()) {
            model.addAttribute("jwt", jwtToken);
        }
        return "projects/dashboard";
    }

    //if to keep old/dashboard URL working
   /* @GetMapping("/dashboard")
    public String redirectOld(){
        return "redirect:/projects/dashboard";
    }*/
}
