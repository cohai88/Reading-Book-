package com.example.api.resource.admin;


import com.example.api.model.dto.LoginRequest;
import com.example.api.model.dto.RegisterRequest;
import com.example.api.model.entity.User;
import com.example.api.model.enums.UserRole;
import com.example.api.security.JwtResponse;
import com.example.api.security.JwtUtil;
import com.example.api.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/user")
public class UserResource {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResource(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (user == null) {
            return new ResponseEntity<>("Invalid login credentials!", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }

        if (userService.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(UserRole.USER);
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list-user")
    public ResponseEntity<?> listUser(@RequestParam(defaultValue = "0")int page,
                                        @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findAllByDeletedFalse(pageable);
        if (!userPage.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userPage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest registerRequest) {

        // Kiểm tra tên người dùng đã tồn tại chưa
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken!");
        }

        // Kiểm tra số điện thoại đã được sử dụng chưa
        if (userService.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if ("ADMIN".equalsIgnoreCase(registerRequest.getRole())) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        user.setFullName(registerRequest.getFullName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEmail(registerRequest.getEmail());

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
        Optional<User> userOpt = userService.findByIdAndDeletedFalse(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Endpoint để chỉnh sửa thông tin của một người dùng đã tồn tại
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody RegisterRequest registerRequest) {
        Optional<User> userOpt = userService.findByIdAndDeletedFalse(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(registerRequest.getUsername());
            user.setFullName(registerRequest.getFullName());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setEmail(registerRequest.getEmail());
            userService.save(user);

            return ResponseEntity.ok().body("User updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleted-user/{id}")
    public ResponseEntity<?>deletedUser(@PathVariable Long id){
        Optional<User>userOpt = userService.findByIdAndDeletedFalse(id);
        if (userOpt.isPresent()){
            User user = userOpt.get();
            user.setDeleted(true);
            userService.save(user);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
