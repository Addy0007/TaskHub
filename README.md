# TaskHub
# TaskHub
Task Management - Task Module

This module focuses only on managing tasks within a project.
- Task: Represents a task with attributes such as taskId, taskName, taskDescription, projectId, creatorId, assignedToId, status, priority, category, dueDate, and createdBy.
- TaskStatus: Represents the status of a task, which can be TODO, IN_PROGRESS, or DONE.
- TaskPriority: Represents the priority of a task, which can be LOW, MEDIUM, or HIGH.
- TaskCategory: Represents the category of a task, which can be FRONTEND, BACKEND, or FULLSTACK.


---
API Documentation
The Task Management System provides the following APIs:
- GET /tasks: Retrieves a list of all tasks.
- GET /tasks/{taskId}: Retrieves a task by its ID.
- GET /tasks/create: Displays a form to create a new task.
- POST /tasks/create: Creates a new task.
- GET /tasks/update/{taskId}: Displays a form to update an existing task.
- POST /tasks/update/{taskId}: Updates an existing task.
- GET /tasks/delete/{taskId}: Deletes a task by its ID.

Task Service
- getAllTasks(): Retrieves a list of all tasks.
- getTaskById(Long taskId): Retrieves a task by its ID.
- createTask(Task task): Creates a new task.
- updateTask(Task task): Updates an existing task.
- deleteTask(Long taskId): Deletes a task by its ID.

✨ Features

- Create, update, delete tasks
- Assign tasks to users 
- Link tasks to a project (mandatory)
- Set task status: `TODO`, `IN_PROGRESS`, `DONE`
- Set priority: `LOW`, `MEDIUM`, `HIGH`
- Set due date

---

💾 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- H2 Embedded Database
- Thymeleaf (for frontend)
- Maven
<<<<<<< HEAD
=======


>>>>>>> b063974e5737eacfd1f70027d599cb6ae2e2f55a
