package com.aithinkers.TaskHub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

	private String username;
	private String token;
	private List<String> roles;
	private String message;
}
