package com.aithinkers.TaskHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aithinkers.TaskHub.entity.User;

public interface RegisterUserRepo extends JpaRepository<User, Integer> {

}
