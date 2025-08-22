package com.taskhub.taskmanagement.repository;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskNameAndTaskDescriptionAndAssignedToId(String taskName, String taskDescription, Long assignedToId);
    List<Task> findByTaskNameAndTaskDescriptionAndCategoryAndProjectIdAndAssignedToId(
            String taskName, String taskDescription, TaskCategory category, Long projectId, Long assignedToId);
    @Query("SELECT t FROM Task t WHERE t.taskName = :taskName AND t.taskDescription = :taskDescription AND t.projectId = :projectId AND t.category = :category")
    List<Task> findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
            @Param("taskName") String taskName,
            @Param("taskDescription") String taskDescription,
            @Param("projectId") Long projectId,
            @Param("category") TaskCategory category);
}
