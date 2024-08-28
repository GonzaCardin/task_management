package com.gonza.task_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gonza.task_management.model.dto.TaskDTO;
import com.gonza.task_management.model.dto.TaskRequest;
import com.gonza.task_management.model.dto.Response;
import com.gonza.task_management.model.entity.Task;
import com.gonza.task_management.model.entity.TaskHistory;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.model.types.TaskPriority;
import com.gonza.task_management.model.types.TaskStatus;
import com.gonza.task_management.repository.TaskRepository;
import com.gonza.task_management.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryService taskHistoryService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Response<Task> createTask(TaskDTO taskDTO, Long userId) {
        Response<Task> response = new Response<>();
        try {
            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setStatus(TaskStatus.TODO);

            User assignedTo = userRepository.findById(taskDTO.getAssignedTo())
                    .orElseThrow(() -> new Exception("Assigned user not found"));
            task.setAssignedTo(assignedTo);
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            taskRepository.save(task);

            User user = userValidation(userId);
            taskHistoryService.createTaskHistory(task, "Task created", user);

            response.setData(task);
            response.setSuccess(true);
            response.setMessage("Task created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error creating task: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public Response<Task> updateTask(Long taskId, TaskDTO taskRequest, Long userId) {
        Response<Task> response = new Response<Task>();
        try {
            Task task = taskValidation(taskId);

            if (!task.getTitle().equals(taskRequest.getTitle()))
                task.setTitle(taskRequest.getTitle());
            if (!task.getDescription().equals(taskRequest.getDescription()))
                task.setDescription(taskRequest.getDescription());
            if (!task.getStatus().equals(taskRequest.getStatus()))
                task.setStatus(taskRequest.getStatus());
            if (task.getAssignedTo() != null) {
                Long assignedTo = taskRequest.getAssignedTo();
                if (!assignedTo.equals(task.getAssignedTo().getId()))
                    task.setAssignedTo(userRepository.findById(assignedTo)
                            .orElseThrow(() -> new Exception("Assigned User not found")));
            }
            if (!task.getDueDate().equals(taskRequest.getDueDate()))
                task.setDueDate(taskRequest.getDueDate());
            if (!task.getPriority().equals(taskRequest.getPriority()))
                task.setPriority(taskRequest.getPriority());
            taskRepository.save(task);

            User user = userValidation(userId);
            taskHistoryService.createTaskHistory(task, "Task updated", user);

            response.setData(task);
            response.setSuccess(true);
            response.setMessage("Task updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error updating task: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public Response<Task> updateTaskHistory(Long taskId, TaskRequest taskRequest, Long userId) {
        Response<Task> response = new Response<Task>();
        try {
            Task task = taskValidation(taskId);

            if (!task.getStatus().equals(taskRequest.getStatus())) {
                task.setStatus(taskRequest.getStatus());
                taskRepository.save(task);
            }

            User user = userValidation(userId);
            taskHistoryService.createTaskHistory(task, taskRequest.getChanges(), user);

            response.setData(task);
            response.setSuccess(true);
            response.setMessage("Task updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error updating task: " + e.getMessage());
        }
        return response;
    }

    @Transactional
    public Response<Task> deleteTask(Long id) {
        Response<Task> response = new Response<Task>();
        try {
            Task task = taskValidation(id);

            taskRepository.delete(task);

            response.setSuccess(true);
            response.setMessage("Task deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error deleting task: " + e.getMessage());
        }
        return response;
    }

    public Response<Task> getTaskById(Long id) {
        Response<Task> response = new Response<Task>();
        try {
            Task task = taskValidation(id);
            response.setSuccess(true);
            response.setMessage("Task found successfully");
            response.setData(task);
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error finding task: " + e.getMessage());
        }
        return response;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByAssignedTo(Long userId) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found");
        }
        return taskRepository.findByAssignedToId(userId);
    }

    public List<TaskHistory> getTaskHistory(Long taskId) {
        return taskHistoryService.getTaskHistoryByTaskId(taskId);
    }

    public List<Task> getTaskByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("User not found");
        }
        return taskRepository.findByAssignedToId(userId);
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    private User userValidation(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private Task taskValidation(Long taskId){
        return taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }
}
