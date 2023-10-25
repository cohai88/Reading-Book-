package com.example.api.model.dto;

import com.example.api.model.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String Role;
    private String fullName;
    private String email;
    private String phoneNumber;
}
