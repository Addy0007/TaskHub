package com.taskhub.taskmanagement.controller;

import org.springframework.ui.Model;

import com.taskhub.taskmanagement.entity.Task;
import com.taskhub.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks";
    }

    @GetMapping("/{taskId}")
    public String getTaskById(@PathVariable Long taskId, Model model) {
        model.addAttribute("task", taskService.getTaskById(taskId));
        return "task";
    }

    @GetMapping("/create")
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "create-task";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task,Model model) {
        if (task == null) {
            model.addAttribute("errorMessage", "Task cannot be null");
            return "create-task";
        }
        try {
            taskService.createTask(task);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
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

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }


}
