package com.aithinkers.TaskHub.Service;

import com.aithinkers.TaskHub.Entity.Project;
import com.aithinkers.TaskHub.Entity.User;
import com.aithinkers.TaskHub.Repository.ProjectRepository;
import com.aithinkers.TaskHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    /* we are using private final here as Construction Injection
     * since Spring 4.3 if a class has only one constructor ,spring will autmatically use it for
     * dependency injection even without @Autowired
     * Here Lambok's @requriedArgsconstructor generates that constructor for us
     * autowired best demo projects and private final lomboks constructor is good for production  */

    private final ProjectRepository projectRepository;
    private final UserRepository userRepo;

    //Transactional -> use it were we its performing update operations like to save,storing objects in the DataBase
    //Transactional guarantees atomic updates and lets hibernate flush collection changes automatically
    /* Here ->Fetches the project (with members)
    * Fetches the user(by email) And adds the user into the project's members collection
    * and saves the project back
    * ->Working
    *1.	Call repository method findWithMembers(projectId) → returns Optional<Project>.
	2.	If a project is found, extract the Project inside the Optional.
	3.	If no project is found, throw IllegalArgumentException("Project not found").
	4.	Assign the result into p (which is of type Project).
        So p is just a fully-loaded Project entity
    *->findWithMembers is your custom repo method to fetch a project and its members.
    *We use findWithMembers here because we’re about to modify the project’s members collection.
    *  For that, we need the collection to be fully loaded and managed by Hibernate. This way, when we call save(p),
    *  Hibernate knows exactly what to insert into the join table.*/

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")  //Only Admins can assign
    public void addMemberByEmail(Long projectId, String email){
        Project p = projectRepository.findWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        p.getMembers().add(u);   //  assign user to project
        projectRepository.save(p);
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void removeMember(Long projectId, Long userId) {
        Project p = projectRepository.findWithMembers(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        User u = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        p.getMembers().remove(u);   //  unassigned or Remove user from project
        projectRepository.save(p);
    }
}
