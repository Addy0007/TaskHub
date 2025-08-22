package com.aithinkers.TaskHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.aithinkers.TaskHub.entity.User;

public interface RegisterUserRepo extends JpaRepository<User, Integer> {
	Optional<User> findByName(String name);
}
