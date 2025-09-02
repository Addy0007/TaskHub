package com.aithinkers.TaskHub.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aithinkers.TaskHub.entity.User;
import com.aithinkers.TaskHub.repository.RegisterUserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final RegisterUserRepo registerUserRepo;
    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);
        
        User user = registerUserRepo.findByName(username)
            .orElseThrow(() -> {
                logger.error("User not found in database: {}", username);
                return new UsernameNotFoundException("User not found: " + username);
            });

        logger.debug("User found: {} with role: {}", user.getName(), user.getRole());
        
        Collection<GrantedAuthority> authorities = parseAuthorities(user.getRole());

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getName())
            .password(user.getPassword())
            .authorities(authorities)
            .accountLocked(false)
            .accountExpired(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

    private Collection<GrantedAuthority> parseAuthorities(String roleField) {
        if (roleField == null || roleField.isBlank()) {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Arrays.stream(roleField.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}


