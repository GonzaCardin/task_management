package com.gonza.task_management.model.dto;

import com.gonza.task_management.model.types.TaskStatus;

import lombok.Data;

@Data
public class TaskRequest {
    private TaskStatus status;
    private String changes;
}
