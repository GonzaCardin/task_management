package com.gonza.task_management.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gonza.task_management.model.dto.TaskDTO;
import com.gonza.task_management.model.dto.TaskRequest;
import com.gonza.task_management.model.dto.TaskResponse;
import com.gonza.task_management.model.entity.Task;
import com.gonza.task_management.model.entity.TaskHistory;
import com.gonza.task_management.model.types.TaskPriority;
import com.gonza.task_management.model.types.TaskStatus;
import com.gonza.task_management.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD')")
    @PostMapping("/createTask")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        TaskResponse response = taskService.createTask(taskDTO, userId);
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest,
            @RequestParam Long userId) {
        TaskResponse response = taskService.updateTask(id, taskRequest, userId);
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id) {
        TaskResponse response = taskService.deleteTask(id);
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // TODO: analize this method, maybe it's not necessary
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse response = taskService.getTaskById(id);
        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping("/mytasks")
    public ResponseEntity<List<Task>> getMyTasks(@RequestParam Long userId) {
        List<Task> tasks = taskService.getTaskByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable TaskPriority priority) {
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    // TODO: analize this method, maybe it's not necessary
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD')")
    @GetMapping("/assignedTo/{userId}")
    public ResponseEntity<List<Task>> getTasksByAssignedTo(@PathVariable Long userId) throws Exception {
        List<Task> tasks = taskService.getTasksByAssignedTo(userId);
        return ResponseEntity.ok(tasks);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasRole('TEAM_LEAD') or hasRole('DEVELOPER')")
    @GetMapping("/history/{taskId}")
    public ResponseEntity<List<TaskHistory>> getTaskHistory(@PathVariable Long taskId) {
        List<TaskHistory> history = taskService.getTaskHistory(taskId);
        if (history != null && !history.isEmpty()) {
            return ResponseEntity.ok(history);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }
}
