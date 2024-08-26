package com.gonza.task_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.gonza.task_management.model.dto.TaskDTO;
import com.gonza.task_management.model.dto.TaskRequest;
import com.gonza.task_management.model.dto.TaskResponse;
import com.gonza.task_management.model.entity.Task;
import com.gonza.task_management.model.entity.TaskHistory;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.model.types.TaskPriority;
import com.gonza.task_management.model.types.TaskStatus;
import com.gonza.task_management.repository.TaskHistoryRepository;
import com.gonza.task_management.repository.TaskRepository;
import com.gonza.task_management.repository.UserRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskHistoryRepository taskHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(TaskDTO taskDTO, Long userId) {
        TaskResponse response = new TaskResponse();
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

            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setTask(task);
            taskHistory.setChanges("Task created");
            taskHistory.setCreatedBy(user);
            taskHistory.setChangedBy(user);
            taskHistoryRepository.save(taskHistory);

            response.setTask(task);
            response.setSuccess(true);
            response.setMessage("Task created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("Error creating task: " + e.getMessage());
        }
        return response;
    }

    // TODO: method 1: updateTask for A/PM/TL || method 2: updateTask for
    // Developer(only can update status and task history)
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest, Long userId) {
        TaskResponse response = new TaskResponse();
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task not found"));

            if (!task.getStatus().equals(taskRequest.getStatus())) {
                task.setStatus(taskRequest.getStatus());
                taskRepository.save(task);
            }

            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setTask(task);
            taskHistory.setChanges(taskRequest.getChanges());
            taskHistory.setCreatedBy(task.getAssignedTo());
            taskHistory.setChangedBy(user);
            taskHistoryRepository.save(taskHistory);

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
    public TaskResponse deleteTask(Long id) {
        TaskResponse response = new TaskResponse();
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found"));

            taskHistoryRepository.deleteByTaskId(id);
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

    public TaskResponse getTaskById(Long id) {
        TaskResponse response = new TaskResponse();
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new Exception("Task not found"));
            response.setSuccess(true);
            response.setMessage("Task found successfully");
            response.setTask(task);
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
            throw new Exception("User not found");
        }
        return taskRepository.findByAssignedToId(userId);
    }

    public List<TaskHistory> getTaskHistory(Long taskId) {
        return taskHistoryRepository.findByTaskId(taskId);
    }

    public List<Task> getTaskByUserId(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return taskRepository.findByAssignedToId(userId);
    }

    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }
}
