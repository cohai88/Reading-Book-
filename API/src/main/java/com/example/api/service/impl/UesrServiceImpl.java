package com.example.api.service.impl;

import com.example.api.model.entity.User;
import com.example.api.model.enums.UserRole;
import com.example.api.repository.UserRepository;
import com.example.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UesrServiceImpl implements UserService {
    private final UserRepository userRepository;



    public UesrServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(String username, String password, String fullName, String email, String phoneNumber) {
        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(UserRole.USER);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setDeleted(false);

        return userRepository.save(newUser);
    }


    @Override
    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }



    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByIdAndDeletedFalse(Long id) {
        return userRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Page<User> findAllByDeletedFalse(Pageable pageable) {
        return userRepository.findAllByDeletedFalse(pageable);
    }
}

