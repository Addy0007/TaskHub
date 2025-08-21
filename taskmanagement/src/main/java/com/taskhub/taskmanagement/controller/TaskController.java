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
    public String createTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/update/{taskId}")
    public String updateTaskForm(@PathVariable Long taskId, Model model) {
        model.addAttribute("task", taskService.getTaskById(taskId));
        return "update-task";
    }

    @PostMapping("/update/{taskId}")
    public String updateTask(@PathVariable Long taskId, @ModelAttribute Task task) {
        task.setTaskId(taskId);

        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/tasks";
    }


}
