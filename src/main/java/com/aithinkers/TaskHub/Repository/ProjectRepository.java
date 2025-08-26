package com.aithinkers.TaskHub.Repository;

import com.aithinkers.TaskHub.Entity.Project;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository  extends JpaRepository<Project,Long> {

    /* Here What does This method:it finds single project byId and also fetches all of its members
    * then the result is kept in Optional<project>

    * select p from Project p
		Selects the Project entity (JPA entity)
		p is just used as an alias here and left join fetch p.members and Joins the project with its members collection (Set<User> from User entity).
		fetch tells Hibernate to load those members immediately in the same query
		->where p.id = :id
		Restricts the query to only the project with the given id parameter.
		*
		* if we use findById(id) it would fetch only project ,But this query fetches project and the members init */

    //@Query is Spring Jpa that lets you JPQL queries directly into Repo
    //here the names not table name they were entity names : JPQL(Entity-Based ,not table-Based)
@Query("""
        select p from Project p
        left join fetch p.members where p.id = :id""")
    Optional<Project> findWithMembers(Long id);

//Here the Query Logic was return the projects were the member was part of District means Unique
    //selects all distinct projects(p) from project entity and Join projects with thier members(p.members)
    //and filter where the members id = userid and then ORDERS the resulting projects by projectId in descending order

@Query("""
        SELECT DISTINCT p FROM Project p 
        JOIN p.members m 
        WHERE m.id = :userId
        ORDER BY p.id DESC""")
List<Project> findAllByMemberId(@Param("userId") Long userId);
}
