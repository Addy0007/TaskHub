package com.taskhub.taskmanagement.repository;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.entity.TaskCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TaskRepositoryTest {
    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testFindByTaskNameAndTaskDescriptionAndAssignedToId() {
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setAssignedToId(1L);
        when(taskRepository.findByTaskNameAndTaskDescriptionAndAssignedToId(
                "Test Task", "Test Description", 1L))
                .thenReturn(List.of(task));

        List<Task> result = taskRepository.findByTaskNameAndTaskDescriptionAndAssignedToId(
                "Test Task", "Test Description", 1L);

        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(taskRepository, times(1)).findByTaskNameAndTaskDescriptionAndAssignedToId(
                "Test Task", "Test Description", 1L);

    }

    @Test
    public void testFindByTaskNameAndTaskDescriptionAndCategoryAndProjectIdAndAssignedToId() {
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setCategory(TaskCategory.FRONTEND);
        task.setProjectId(1L);
        task.setAssignedToId(1L);
        when(taskRepository.findByTaskNameAndTaskDescriptionAndCategoryAndProjectIdAndAssignedToId(
                "Test Task", "Test Description", TaskCategory.FRONTEND, 1L, 1L))
                .thenReturn(List.of(task));

        List<Task> result = taskRepository.findByTaskNameAndTaskDescriptionAndCategoryAndProjectIdAndAssignedToId(
                "Test Task", "Test Description", TaskCategory.FRONTEND, 1L, 1L);

        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(taskRepository, times(1)).findByTaskNameAndTaskDescriptionAndCategoryAndProjectIdAndAssignedToId(
                "Test Task", "Test Description", TaskCategory.FRONTEND, 1L, 1L);

    }

    @Test
    public void testFindByTaskNameAndTaskDescriptionAndProjectIdAndCategory() {
        Task task = new Task();
        task.setTaskName("Test Task");
        task.setTaskDescription("Test Description");
        task.setProjectId(1L);
        task.setCategory(TaskCategory.FRONTEND);
        when(taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND))
                .thenReturn(List.of(task));

        List<Task> result = taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND);

        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(taskRepository, times(1)).findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND);

    }

    @Test
    public void testFindByTaskNameAndTaskDescriptionAndProjectIdAndCategoryNoResult() {
        when(taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND))
                .thenReturn(List.of());

        List<Task> result = taskRepository.findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND);

        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findByTaskNameAndTaskDescriptionAndProjectIdAndCategory(
                "Test Task", "Test Description", 1L, TaskCategory.FRONTEND);

    }

}
