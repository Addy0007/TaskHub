package com.aithinkers.TaskHub.service;

import org.springframework.http.ResponseEntity;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.dto.LoginResponse;
import com.aithinkers.TaskHub.dto.SignUpRequest;

public interface TaskHubService {
	
	String registerTheUser(SignUpRequest signUpRequest);
	LoginResponse authenticateTheUser(LoginRequest loginRequest);
	SignUpRequest getUserDetailsForUpdate(String username);
	String updateUserProfile(String username, SignUpRequest signUpRequest);

}
