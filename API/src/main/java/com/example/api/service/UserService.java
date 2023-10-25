package com.example.api.service;

import com.example.api.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User registerUser(String username, String password, String fullName, String email, String phoneNumber);
    User authenticateUser(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    void save(User user);
    Optional<User> findByIdAndDeletedFalse(Long id);
    void delete(User user);
    Page<User> findAllByDeletedFalse(Pageable pageable);

}
