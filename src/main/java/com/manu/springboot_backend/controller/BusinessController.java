package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.StoreDTO;
import com.manu.springboot_backend.model.Business;
import com.manu.springboot_backend.model.User;
import com.manu.springboot_backend.repository.StoreRepository;
import com.manu.springboot_backend.repository.UserRepository;
import com.manu.springboot_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.manu.springboot_backend.model.ProfileStatus.ACTIVE;
import static com.manu.springboot_backend.model.ProfileStatus.INACTIVE;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Business Management", description = "Endpoints for managing businesses")
@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public BusinessController(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint to onboard a business when a user signs up for the first time.
     * This creates or updates a business associated with the logged-in admin user.
     *
     * @param storeDTO Business details from the request body
     * @return ResponseEntity with a message and status
     */
    @Operation(
            summary = "Onboard a business",
            description = "Allows a new user to create a business during onboarding, before they can create anything else ")
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrUpdateStore(@RequestBody StoreDTO storeDTO) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail).orElse(null);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin user not found"));
        }

        Optional<Business> existingStoreOpt = storeRepository.findByName(storeDTO.getName());

        if (existingStoreOpt.isPresent()) {
            Business existingBusiness = existingStoreOpt.get();

            if ("Y".equals(existingBusiness.getDeletedFlag())) {
                existingBusiness.setDeletedFlag("N");
                existingBusiness.setDescription(storeDTO.getDescription());
                existingBusiness.setEmail(storeDTO.getEmail());
                existingBusiness.setContactNumber(storeDTO.getContactNumber());
                existingBusiness.setName(storeDTO.getName());
                existingBusiness.setModifiedBy(loggedInUser);
                existingBusiness.setModifiedTime(LocalDateTime.now());

                storeRepository.save(existingBusiness);
                return ResponseEntity.ok(Map.of("message", "Business restored and updated successfully", "status", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.ok(Map.of("message", "Business already exists", "status", HttpStatus.OK.value()));
            }
        }

        Business business = new Business();
        business.setName(storeDTO.getName());
        business.setLocation(storeDTO.getLocation());
        business.setDescription(storeDTO.getDescription());
        business.setContactNumber(storeDTO.getContactNumber());
        business.setPostedBy(loggedInUser);
        business.setPostedTime(LocalDateTime.now());
        business.setPostedFlag("Y");

        storeRepository.save(business);

        if (loggedInUser.getStatus() != null && INACTIVE.equals(loggedInUser.getStatus())) {
            loggedInUser.setStatus(ACTIVE);
            userRepository.save(loggedInUser);
        }

        return ResponseEntity.ok(Map.of("message", "Business added successfully", "status", HttpStatus.CREATED.value()));
    }

    /**
     * Checks if the logged-in user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        return userRepository.findByEmail(loggedInEmail)
                .map(user -> user.getRole().name().equals("ADMIN"))
                .orElse(false);
    }
}
