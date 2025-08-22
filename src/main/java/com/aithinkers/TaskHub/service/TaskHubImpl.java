package com.aithinkers.TaskHub.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.dto.LoginResponse;
import com.aithinkers.TaskHub.dto.SignUpRequest;
import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.jwt.JwtUtils;
import com.aithinkers.TaskHub.repository.RegisterUserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskHubImpl implements TaskHubService {

	private final RegisterUserRepo repo;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	@Override
	public String registerTheUser(SignUpRequest signUpRequest) {
		User user = new User();
		user.setName(signUpRequest.getName());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setRole(signUpRequest.getRole());
		user.setEmail(signUpRequest.getEmail());

		repo.save(user);

		return "User " + user.getName() + " saved successfully";
	}

	@Override
	public LoginResponse authenticateTheUser(LoginRequest loginRequest) {

	    Authentication authentication;

	    try {
	        authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                		loginRequest.getUserName(), loginRequest.getPassword()));
	    } catch (AuthenticationException exception) {
	        throw new RuntimeException("Bad credentials");
	    }

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
	    List<String> roles = userDetails.getAuthorities()
	            									.stream()
	            										.map(item -> item.getAuthority())
	            											.collect(Collectors.toList());

	   
	    return new LoginResponse(userDetails.getUsername(),jwtToken,roles,"Login successful!");
	  
	}
	
	@Override
	public SignUpRequest getUserDetailsForUpdate(String username) {
	    User user = repo.findByName(username)
	            .orElseThrow(() -> new RuntimeException("User not found: " + username));
	    
	    SignUpRequest signUpRequest = new SignUpRequest();
	    signUpRequest.setName(user.getName());
	    signUpRequest.setEmail(user.getEmail());
	    signUpRequest.setRole(user.getRole());
	    signUpRequest.setPassword(user.getPassword()); 

	    return signUpRequest;
	}
	
	@Override
	public String updateUserProfile(String username, SignUpRequest signUpRequest) {
	    User user = repo.findByName(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setName(signUpRequest.getName());
	    user.setEmail(signUpRequest.getEmail());
	    
	    if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isBlank()) {
	        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
	    }

	    repo.save(user);
	    
	    return "Profile updated successfully!";
	}



}
