package com.aithinkers.TaskHub.Exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public String handleDenied(AccessDeniedException ex, HttpServletRequest req, RedirectAttributes ra){
        ra.addFlashAttribute("error", "Only admins can perform this action.");
        String back = req.getHeader("Referer");
        return "redirect:" + (back != null ? back : "/projects");
    }

}
