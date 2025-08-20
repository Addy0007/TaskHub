package com.aithinkers.TaskHub.service;

import org.springframework.http.ResponseEntity;

import com.aithinkers.TaskHub.dto.LoginRequest;

public interface TaskHubService {
	
	String registerTheUser(LoginRequest loginRequest);

}
