package com.aithinkers.TaskHub.entity;

import com.aithinkers.TaskHub.Enum.ProjectType;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType;

   @ManyToOne
    @JoinColumn(name = "created_by"
    )
    private User createdBy;

  /*  this refers to project entity
    jointable with name="project_members"-table name
    join columns=@joincolumn(name='project_id")
            inverseJoinColumns = @joinColumn("userid")
    ->this projectmembers will be middle table combining or using both the other tables
    pk(Primary Key) or columns*/

    //This was an Link between Project <-> User
    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    //builder() a lombark function
    //that generates static builder,you can construct objects clean and simple like above
    //or we need use EX:User U=new User();->u.setEmail(email)etc
}
