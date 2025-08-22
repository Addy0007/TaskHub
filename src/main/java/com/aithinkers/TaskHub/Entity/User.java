package com.aithinkers.TaskHub.Entity;

import com.aithinkers.TaskHub.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

//builder() a lombark function
//that generates static builder,you can construct objects clean and simple like above
//or we need use EX:USer U=new User();->u.setEmail(email)etc

@Entity
@Table(name = "users")
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 100)
    private String name;
    @Column(nullable = false, unique = true, length = 250)
    private String email;
    @Column(nullable = false, length=255)
    private String password;
//here enum was created with Roles user and Admin
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = Role.USER;

    /*Projects is a collection of project entities
    many to many here one user belong to many projects  and one project can
    have many users
    ->why set instead of list because in the same project being linked multiple
    time to same user
    and JPA prefers sets for many to many
    ->it will store the project objects linked to that user in the database
    ->we have to sides owning & inverse side this inverse points it back to owning side field
    this refers to project entity
    jointable with name="project_members"-table name
    join columns=@joincolumn(name='project_id")
    inverseJoinColumns = @joinColumn("userid")
    ->this projectmembers will be middle table combining or using both the other tables
    pk or columns
     */
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();

}
