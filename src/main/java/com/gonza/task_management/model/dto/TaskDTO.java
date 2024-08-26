package com.gonza.task_management.model.dto;

import java.time.LocalDate;

import com.gonza.task_management.model.types.TaskPriority;

import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private Boolean completed;
    private Long assignedTo;
    private TaskPriority priority;
    private LocalDate dueDate;
}
