package com.gonza.task_management.model.dto;

import com.gonza.task_management.model.entity.Task;

import lombok.Data;

@Data
public class TaskResponse {
    private Task task;
    private Boolean success;
    private String message;

    public Boolean isSuccess(){
        return success;
    }
}
