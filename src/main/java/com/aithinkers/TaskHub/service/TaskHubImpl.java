package com.aithinkers.TaskHub.service;

import org.springframework.stereotype.Service;

import com.aithinkers.TaskHub.dto.LoginRequest;
import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.repository.RegisterUserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskHubImpl implements TaskHubService {

    private final RegisterUserRepo repo;

    @Override
    public String registerTheUser(LoginRequest loginRequest) {
        User user = new User();
        user.setName(loginRequest.getName());
        user.setPassword(loginRequest.getPassword());
        user.setRole(loginRequest.getRole());
        user.setEmail(loginRequest.getEmail());

        repo.save(user);

        return "User " + user.getName() + " saved successfully";
    }
}
