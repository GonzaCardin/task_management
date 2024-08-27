package com.gonza.task_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gonza.task_management.model.entity.Task;
import com.gonza.task_management.model.entity.TaskHistory;
import com.gonza.task_management.model.entity.User;
import com.gonza.task_management.repository.TaskHistoryRepository;

@Service
public class TaskHistoryService {
    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    public void createTaskHistory(Task task, String changes, User user) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTask(task);
        taskHistory.setChanges(changes);
        taskHistory.setCreatedBy(user);
        taskHistory.setChangedBy(user);
        taskHistoryRepository.save(taskHistory);
    }

    public List<TaskHistory> getTaskHistoryByTaskId(Long taskId) {
        return taskHistoryRepository.findByTaskId(taskId);
    } 


}
