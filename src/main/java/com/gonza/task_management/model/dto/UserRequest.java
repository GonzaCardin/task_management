package com.gonza.task_management.model.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String newEmail;
    private String password;
    private String newPassword;
}
