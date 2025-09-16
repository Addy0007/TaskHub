package com.aithinkers.TaskHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.aithinkers.TaskHub.entity.User;

/**
 * Repository interface for User entity operations
 * Provides methods to interact with the User table in the database
 */
public interface RegisterUserRepo extends JpaRepository<User, Integer> {
	
	/**
	 * Find user by username
	 * @param name username to search for
	 * @return Optional containing user if found
	 */
	Optional<User> findByName(String name);
	
	/**
	 * Find user by email address
	 * @param email email address to search for
	 * @return Optional containing user if found
	 */
	Optional<User> findByEmail(String email);

	boolean existsByName(String name);
	boolean existsByEmail(String email);
}
