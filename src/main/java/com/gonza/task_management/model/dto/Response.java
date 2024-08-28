package com.gonza.task_management.model.dto;

import lombok.Data;

@Data
public class Response<T> {
    private T data;
    private Boolean success;
    private String message;

    public Boolean isSuccess() {
        return success;
    }

}
