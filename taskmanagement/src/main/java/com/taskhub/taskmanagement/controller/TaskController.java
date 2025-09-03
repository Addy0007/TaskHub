package com.taskhub.taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Operation(summary = "get task by its id")
    @GetMapping("/{taskId}")
    public String getTaskById(@PathVariable Long taskId, Model model) {
        model.addAttribute("task", taskService.getTaskById(taskId));
        return "task";
    }
    @GetMapping("/tasks")
    public String getAllTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }


    @GetMapping("/create")
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create-task";
    }
    @Operation(summary = "create a task")
    @PostMapping("/create")
    public String createTask(@Valid @ModelAttribute Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return "create-task";
        }
        try {
            taskService.createTask(task);
            System.out.println("Task created successfully!");
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            System.out.println("Error creating task: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("task", task);
            return "create-task";
        }


    }

    @GetMapping("/update/{taskId}")
    public String updateTaskForm(@PathVariable Long taskId, Model model) {
        model.addAttribute("task", taskService.getTaskById(taskId));
        return "update-task";
    }
    @Operation(summary = "update a task")
    @PostMapping("/update/{taskId}")
    public String updateTask(@PathVariable Long taskId, @ModelAttribute Task task,Model model) {
        if (task == null|| task.getTaskName() == null) {
            return "redirect:/tasks"; // or return an error view
        }
        try {
            taskService.updateTask(task);
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "update-task";
        }

    }
    @Operation(summary = "delete a task")
    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }
    @Operation(summary = "get all tasks or get task based on search ")
    @GetMapping
    public String getTasks(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            List<Task> tasks = taskService.searchTasks(query);
            model.addAttribute("tasks", tasks);
        } else {
            List<Task> tasks = taskService.getAllTasks();
            model.addAttribute("tasks", tasks);
        }
        return "tasks";
    }



}
