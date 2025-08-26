package com.aithinkers.TaskHub.Repository;

import com.aithinkers.TaskHub.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    //Here the query returns the projects tasks and also the project assignees
    /* select distinct t – because a task can have multiple assignees, the join would otherwise return duplicate task rows;
     distinct collapses them to unique Tasks
     from Task t – work with Task entities.
	left join fetch t.assignees a – join the assignees collection and fetch it eagerly to avoid an N+1 query problem. left keeps tasks that have no assignees.
	where t.projectId = :projectId (or t.project.id = :projectId) – filter tasks that belong to the given project.
	order by t.id desc – sort tasks newest first*/

    @Query("""
           select distinct t
           from Task t
           left join fetch t.assignees a
           where t.project.id = :projectId
           order by t.id desc
           """)
    List<Task> findAllByProjectIdWithAssignees(@Param("projectId") Long projectId);

}
