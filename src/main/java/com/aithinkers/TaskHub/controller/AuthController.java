package com.aithinkers.TaskHub.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.dto.LoginResponse;
import com.aithinkers.TaskHub.dto.SignUpRequest;
import com.aithinkers.TaskHub.service.TaskHubImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final TaskHubImpl service;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("signupRequest", new SignUpRequest());
		return "register";
	}

	@PostMapping("/register")
	public String registerTheUser(SignUpRequest signUpRequest, Model model) {
		String message = service.registerTheUser(signUpRequest);
		model.addAttribute("message", message);
		model.addAttribute("signupRequest", new SignUpRequest());
		return "register";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	@PostMapping("/login")
	public String loginTheUser(@ModelAttribute LoginRequest loginRequest, Model model) {
		try {
			LoginResponse response = service.authenticateTheUser(loginRequest);
			model.addAttribute("response", response);
			model.addAttribute("token", response.getToken());
			return "welcome";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
	}
	
	@GetMapping("/update")
	public String updateTheProfile(Model model, Principal principal) {
	    String username = principal.getName();
	    SignUpRequest signUpRequest = service.getUserDetailsForUpdate(username);
	    model.addAttribute("signUpRequest", signUpRequest);
	    return "viewandupdate";
	}
	
	@PostMapping("/update")
	public String updateProfile(@ModelAttribute SignUpRequest signUpRequest, Model model, Principal principal) {
	    String username = principal.getName();

	    String message = service.updateUserProfile(username, signUpRequest);
	    model.addAttribute("message", message);

	    SignUpRequest updatedData = service.getUserDetailsForUpdate(username);
	    model.addAttribute("signUpRequest", updatedData);
	   

	    return "update-success";
	}



}
