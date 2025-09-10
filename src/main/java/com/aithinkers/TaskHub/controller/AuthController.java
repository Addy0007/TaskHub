package com.aithinkers.TaskHub.controller;

import java.security.Principal;
import java.util.List;

import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.dto.LoginResponse;
import com.aithinkers.TaskHub.dto.SignUpRequest;
import com.aithinkers.TaskHub.service.TaskHubImpl;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsible for handling authentication-related requests
 * including user registration, login, and profile management
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final TaskHubImpl service;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signupRequest", new SignUpRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerTheUser(@ModelAttribute SignUpRequest signUpRequest, Model model) {
        try {
            String message = service.registerTheUser(signUpRequest);
            model.addAttribute("message", message);
            model.addAttribute("signupRequest", new SignUpRequest());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("signupRequest", signUpRequest); // Keep the form data
        }
        return "register";
    }


   @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    @PostMapping("/login")
    public String doLogin(@ModelAttribute LoginRequest loginRequest,
                          HttpServletResponse response,
                          Model model) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetails ud = (UserDetails) auth.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(ud);

        //send JWT back in response header
        response.setHeader("Authorization","Bearer " + jwt);

        ResponseCookie cookie = ResponseCookie.from("JWT", jwt)
                .httpOnly(true).path("/").sameSite("Lax").maxAge(4*60*60).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Put whatever you want to show on home
        model.addAttribute("response", new LoginResponse(ud.getUsername(), jwt,
                ud.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                "Login successful!"));

        return "home"; // Thymeleaf template: templates/home.html
    }

    @PostMapping("/home")
    public String loginTheUser(@ModelAttribute LoginRequest loginRequest, Model model) {
        try {
            // Authenticate user and generate JWT token
            LoginResponse response = service.authenticateTheUser(loginRequest);
            model.addAttribute("response", response);
            return "home";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Invalid username or password");
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(name = "jwt_token", required = false) String jwt,
            @AuthenticationPrincipal UserDetails me,
            Model model) {

        // Build the same shape your Thymeleaf expects: response.username, response.roles, response.message, response.token
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("username", (me != null ? me.getUsername() : ""));
        response.put("roles", (me != null ?
                me.getAuthorities().stream()
                        .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                        .toList()
                : java.util.List.of()));
        response.put("message", "Login successful");
        response.put("token", jwt);      // allow page to re-store jwt in localStorage

        model.addAttribute("response", response);
        return "home";                   // renders templates/home.html
    }
@GetMapping("/welcome")
        public String welcome(){
        return "redirect:api/auth/home";
}


    /**
     * Displays the welcome page for authenticated users
     * @param model Spring MVC model to add attributes
     * @param principal Current authenticated user
     * @return welcome.html template
     */
//    @GetMapping("/welcome")
//    public String showWelcomePage(Model model, Principal principal) {
//        if (principal != null) {
//            // Get user details and create a response object
//            SignUpRequest userDetails = service.getUserDetailsForUpdate(principal.getName());
//            LoginResponse response = new LoginResponse(
//                userDetails.getName(),
//                "JWT token will be available after login",
//                java.util.Arrays.asList(userDetails.getRole()),
//                "Welcome back!"
//            );
//            model.addAttribute("response", response);
//        } else {
//            // If no principal, redirect to login
//            return "redirect:/api/auth/login";
//        }
//        return "welcome";
//    }


    @GetMapping("/update")
    public String updateTheProfile(Model model, Principal principal) {
        String username = principal.getName();
        SignUpRequest signUpRequest = service.getUserDetailsForUpdate(username);
        model.addAttribute("signUpRequest", signUpRequest);
        return "viewandupdate";
    }


    @PostMapping("/update")
    public String updateProfile(SignUpRequest signUpRequest, Model model, Principal principal) {

        String username = principal.getName();
        String message = service.updateUserProfile(username, signUpRequest);
        model.addAttribute("message", message);
        model.addAttribute("signUpRequest",signUpRequest);
        return "viewandupdate";
    }


    //Get all users
    @GetMapping("/getallusers")
    public String getAllUsers(Model model){

        List<User> users=service.getAllUsers();
        model.addAttribute("users",users);
        return "userlist";
    }

    @GetMapping("/edit-role/{id}")
    public String editUserRole(@PathVariable Integer id, Model model, @RequestParam(value = "jwt_token", required = false) String jwtToken) {
        User user = service.getUserById(id);
        if (user == null) {
            return "redirect:/api/auth/getallusers?error=UserNotFound";
        }
        model.addAttribute("user", user);
        model.addAttribute("jwt_token", jwtToken);
        return "edit-user-role"; // create edit-user-role.html
    }

    @PostMapping("/update-role")
    public String updateUserRole(@RequestParam Integer id, @RequestParam String role, @RequestParam(value = "jwt_token", required = false) String jwtToken) {
        service.updateUserRole(id, role);
        String suffix = jwtToken != null ? ("?jwt_token=" + jwtToken) : "";
        return "redirect:/api/auth/getallusers" + suffix;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id, @RequestParam(value = "jwt_token", required = false) String jwtToken) {
        service.deleteUserById(id);
        String suffix = jwtToken != null ? ("?jwt_token=" + jwtToken) : "";
        return "redirect:/api/auth/getallusers" + suffix;
    }
}
