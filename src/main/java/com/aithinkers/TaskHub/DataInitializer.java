package com.aithinkers.TaskHub;


import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Entity.User;
import com.aithinkers.TaskHub.Enum.ProjectType;
import com.aithinkers.TaskHub.Enum.Role;
import com.aithinkers.TaskHub.Repository.ProjectRepository;
import com.aithinkers.TaskHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

// src/main/java/.../bootstrap/DataInitializer.java
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
           User admin = ensure("admin@example.com", "Admin",  "admin123", Role.ADMIN);
           User sandeep = ensure("sandeep@example.com", "Sandeep","user123", Role.USER);

            if (projectRepo.count() == 0) {
                Project p = new Project();
                p.setName("TaskHub First Project");
                p.setDescription("Sample project created on startup");
                p.setProjectType(ProjectType.TEAM_MANAGED);
                p.setCreatedBy(admin);
                projectRepo.save(p);
            }
        };
    }

    private User ensure(String email, String name, String rawPassword, Role role) {
       return userRepo.findByEmail(email).orElseGet(() ->
            userRepo.save(User.builder()
                    .name(name)
                    .email(email)
                    .password(encoder.encode(rawPassword))
                    .role(role)
                    .build())
        );
    }
}



