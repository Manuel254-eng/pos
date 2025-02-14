//package com.manu.springboot_backend.controller;
//
//import com.manu.springboot_backend.dto.CustomerDTO;
//import com.manu.springboot_backend.model.Role;
//import com.manu.springboot_backend.model.User;
//import com.manu.springboot_backend.repository.UserRepository;
//import com.manu.springboot_backend.security.JwtUtil;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
//@SecurityRequirement(name = "BearerAuth")
//@RestController
//@RequestMapping("/api/v1/customers")
//public class CustomerController {
//
//    private final UserRepository userRepository;
//
//    public CustomerController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    // ✅ Only ADMIN can create a customer
//    @PostMapping
//    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody CustomerDTO customerDTO) {
//        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
//
//        if (userRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));
//        }
//
//        User customer = new User();
//        customer.setName(customerDTO.getName());
//        customer.setEmail(customerDTO.getEmail());
//        customer.setPhone(customerDTO.getPhone());
//        customer.setAddress(customerDTO.getAddress());
//        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
//        customer.setRole(Role.CUSTOMER);
//
//        userRepository.save(customer);
//        return ResponseEntity.ok(Map.of("message", "Customer registered successfully"));
//    }
//
//    // ✅ Only ADMIN can retrieve all customers
//    @GetMapping
//    public ResponseEntity<List<User>> getAllCustomers() {
//        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//
//        return ResponseEntity.ok(userRepository.findByRole(Role.CUSTOMER));
//    }
//
//    // ✅ Only ADMIN can retrieve a customer by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getCustomerById(@PathVariable Long id) {
//        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//
//        Optional<User> customer = userRepository.findById(id);
//        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // ✅ Only ADMIN can update a customer
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateCustomer(@PathVariable Long id, @RequestBody User updatedCustomer) {
//        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//
//        return userRepository.findById(id)
//                .map(customer -> {
//                    customer.setName(updatedCustomer.getName());
//                    customer.setEmail(updatedCustomer.getEmail());
//                    customer.setPhone(updatedCustomer.getPhone());
//                    customer.setAddress(updatedCustomer.getAddress());
//                    return ResponseEntity.ok(userRepository.save(customer));
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // ✅ Only ADMIN can delete a customer
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
//        if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//
//        if (!userRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        userRepository.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // 🔹 Helper method to check if logged-in user is ADMIN
//    private boolean isAdmin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String loggedInEmail = authentication.getName();
//
//        Optional<User> userOpt = userRepository.findByEmail(loggedInEmail);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            System.out.println("Logged-in User: " + user.getEmail());
//            System.out.println("Role: " + user.getRole());
//            return user.getRole() == Role.ADMIN;
//        }
//        return false;
//    }
//}
