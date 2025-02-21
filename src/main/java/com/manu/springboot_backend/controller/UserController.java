package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.UserDTO;
import com.manu.springboot_backend.model.Role;
import com.manu.springboot_backend.model.User;
import com.manu.springboot_backend.repository.UserRepository;
import com.manu.springboot_backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/v1/internal_users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ Only ADMIN can create a cashier
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody UserDTO userDTO) {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied", "status", HttpStatus.FORBIDDEN.value()));

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use", "status", HttpStatus.BAD_REQUEST.value()));
        }

        // Get the logged-in admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        User adminUser = userRepository.findByEmail(loggedInEmail).orElse(null);

        if (adminUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin user not found", "status", HttpStatus.UNAUTHORIZED.value()));
        }

        User user = new User();
        user.setName(userDTO.getUserName());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        user.setPublishedBy(adminUser); // Set the publishedBy field

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User added successfully", "status", HttpStatus.OK.value()));
    }


    // ✅ Only ADMIN can retrieve all customers
    @GetMapping
    public ResponseEntity<List<User>> getAllCustomers() {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        return ResponseEntity.ok(userRepository.findByRole(Role.CASHIER));
    }

    // ✅ Only ADMIN can retrieve a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getCustomerById(@PathVariable Long id) {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        Optional<User> customer = userRepository.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Only ADMIN can update a customer
    @PutMapping("/{id}")
    public ResponseEntity<User> updateCustomer(@PathVariable Long id, @RequestBody User updatedCustomer) {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        return userRepository.findById(id)
                .map(customer -> {
                    customer.setName(updatedCustomer.getName());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setPhone(updatedCustomer.getPhone());
                    customer.setAddress(updatedCustomer.getAddress());
                    return ResponseEntity.ok(userRepository.save(customer));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Only ADMIN can delete a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 🔹 Helper method to check if logged-in user is ADMIN
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(loggedInEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("Logged-in User: " + user.getEmail());
            System.out.println("Role: " + user.getRole());
            return user.getRole() == Role.ADMIN;
        }
        return false;
    }
}
