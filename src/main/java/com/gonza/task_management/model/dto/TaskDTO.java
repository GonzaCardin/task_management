package com.gonza.task_management.model.dto;

import java.time.LocalDate;

import com.gonza.task_management.model.types.TaskPriority;
import com.gonza.task_management.model.types.TaskStatus;

import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedTo;
    private TaskPriority priority;
    private LocalDate dueDate;
}
