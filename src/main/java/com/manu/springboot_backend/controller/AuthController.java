package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.LoginRequest;
import com.manu.springboot_backend.dto.RegisterRequest;
import com.manu.springboot_backend.model.Role;
import com.manu.springboot_backend.model.User;
import com.manu.springboot_backend.repository.UserRepository;
import com.manu.springboot_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//    @PostMapping("/register")
//    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
//        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("status", "error", "message", "Email already in use"));
//        }
//
//        User user = new User();
//        user.setName(registerRequest.getName());
//        user.setEmail(registerRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//
//        // Assign role, default to BORROWER if not provided
//        user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : Role.CUSTOMER);
//
//        User savedUser = userRepository.save(user);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(Map.of(
//                        "status", "success",
//                        "message", "User registered successfully",
//                        "user", Map.of(
//                                "id", savedUser.getId(),
//                                "name", savedUser.getName(),
//                                "email", savedUser.getEmail(),
//                                "role", savedUser.getRole().name()
//                        )
//                ));
//    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> existingUser = userRepository.findByEmail(loginRequest.getEmail());
        if (existingUser.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid email or password"));
        }

        String token = jwtUtil.generateToken(existingUser.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "status", "success",
                        "message", "Login successful",
                        "token", token,
                        "user", Map.of(
                                "id", existingUser.get().getId(),
                                "name", existingUser.get().getName(),
                                "email", existingUser.get().getEmail(),
                                "role", existingUser.get().getRole().name()
                        )
                ));
    }
}
