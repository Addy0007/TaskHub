package com.aithinkers.TaskHub.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Root controller to handle application root endpoint
 * Redirects users to the appropriate page based on their authentication status
 */
@Controller
public class RootController {

    /**
     * Root endpoint - redirects to login page
     * @return redirect to login page
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/api/auth/login";
    }
}
