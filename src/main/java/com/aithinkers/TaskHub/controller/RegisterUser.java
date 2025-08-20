package com.aithinkers.TaskHub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.service.TaskHubImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class RegisterUser {

    private final TaskHubImpl service;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerTheUser(LoginRequest loginRequest, Model model) {
        String message = service.registerTheUser(loginRequest);
        model.addAttribute("message", message);
        return "register-success";
    }
}
