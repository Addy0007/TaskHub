package com.aithinkers.TaskHub.dto;

import lombok.Data;

@Data
public class LoginRequest {
	private Integer id;
	private String name;
	private String email;
	private String password;
	private String role;
}
