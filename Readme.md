# Task Management 

A comprehensive Task Management System built with Java and Spring Boot that facilitates task creation, updates, tracking, and history management. This system leverages role-based security to differentiate access between users and administrators, and it ensures a detailed log of task changes is maintained.

## Features

- **Task Management**: Create, update, and delete tasks with attributes such as title, description, status, and assignment to specific users.
- **Task History Logging**: Automatically logs every change made to tasks, including who made the change and what was changed.
- **Role-Based Access Control**: Implements role-based security (Admin and User roles) using Spring Security.
- **RESTful API**: Provides RESTful endpoints for managing tasks and task history.
- **Transaction Management**: Ensures data integrity with Spring’s `@Transactional` annotation.
- **Error Handling**: Comprehensive error handling to return meaningful responses in case of failures.

## Technologies Used

- **Spring Boot** - Backend framework
- **Spring Security** - For security and authentication
- **JWT** - JSON Web Token for authentication
- **Spring Data JPA** - For data access and persistence
- **MySQL** - Database
- **Maven** - Dependency management and build automation
- **Lombok** - To reduce boilerplate code
- **Hibernate** - ORM tool for data persistence

## Error Handling
The application handles errors and exceptions using Spring's global exception handling mechanism. Common errors include:

- **404 Not Found** - When a resource (e.g., Task) is not found.
- **400 Bad Request** - When input validation fails.
- **401 Unauthorized** - When authentication is required or failed.

## Future Enhancements

#### Frontend Implementation.

As a future implementation, a frontend will be developed to allow users to interact with the task management system through a user-friendly interface. This will be built using modern frontend frameworks like React or Angular, consuming the REST API provided by this backend.

## Additional Features
- Implement notification system for task updates.
- Improve reporting and analytics on task history.
- Add user roles and permissions for more granular access control.
- Project Management System: Plan to implement a Project Management System for enhanced project tracking and organization.