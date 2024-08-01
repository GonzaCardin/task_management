package com.gonza.task_management.model.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private Boolean completed;
    private Long assignedTo;
}
