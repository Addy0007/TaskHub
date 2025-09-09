package com.aithinkers.TaskHub.entity;

import com.aithinkers.TaskHub.Enum.TaskPriority;
import com.aithinkers.TaskHub.Enum.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskTitle;
    private String taskDescription;

    @ManyToOne(fetch = FetchType.LAZY)//One Project can have Many Tasks.By using this JPA understands the relationship( Project<-> Tasks)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)//One user can create many tasks But each task is created by exactly one User
    @JoinColumn(name = "created_by")//->Many tasks -> One user(the creator)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToMany//A Task can be assigned to many users, A user can also be assigned to many Tasks thats why we used many-to-many relationship
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    //Many-to-many : user<->tasks ,jointable defines the middle table
    //task_assignees was table which contains both user-id and task-id so by this one-side we join task_id table and
    // on the other side the join user_id table
    //Each task stores its assigned user inside the Set<user> we used set instead of list to avoid the sam user shouldn't appear twice on one task
    private Set<User> assignees = new HashSet<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
