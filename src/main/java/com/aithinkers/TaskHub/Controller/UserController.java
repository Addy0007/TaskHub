package com.aithinkers.TaskHub.Controller;

import com.aithinkers.TaskHub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    /* we are using private final here as Construction Injection
     * since Spring 4.3 if a class has only one constructor ,spring will autmatically use it for
     * dependency injection even without @Autowired
     * Here Lambok's @requriedArgsconstructor generates that constructor for us
     * autowired best demo projects and private final lomboks constructor is good for production  */
    private final UserService userService;

    /*Here we use Model it acts like an container or A MAP for passing data from Controller(JAVA CODE)
    * to the VIEW(HTML?THYMELEAF) simply it is an DATA BUCKET PASSED TO VIEW .
    * AddAttribute here were userService,findAll()->fetches all users from DB through repo
    * model.addAttribute stores that list in the model under the key "Users".
    * the Return "user/List " the retrun value in sping MVC Controller is interpreted as VIEW NAME  */
    @GetMapping
    public String list(Model model){
        model.addAttribute("users",userService.findAll());
        return "users/userslist";
    }



}
