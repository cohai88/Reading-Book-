package com.example.api.model.entity;

import com.example.api.model.enums.UserRole;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table
@Data
public class User   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false)
    @NotBlank(message = "Full name is required")
    @Size(min = 5, max = 100)
    private String fullName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "[0-9]{10,13}$")
    private String phoneNumber;

    private boolean deleted;


}
