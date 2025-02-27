package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.SupplierDTO;
import com.manu.springboot_backend.dto.SupplierResponseDTO;
import com.manu.springboot_backend.dto.UserResponseDTO;
import com.manu.springboot_backend.model.Supplier;
import com.manu.springboot_backend.model.User;
import com.manu.springboot_backend.model.Role;
import com.manu.springboot_backend.repository.SupplierRepository;
import com.manu.springboot_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/suppliers")
public class SuppliersController {
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public SuppliersController(SupplierRepository supplierRepository, UserRepository userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    // Get all suppliers (Admin Only)
    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        if (!isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        List<SupplierResponseDTO> supplierDTOs = supplierRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(supplierDTOs);
    }




    // Get supplier by ID (Admin Only)
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Optional<Supplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SupplierResponseDTO supplierDTO = convertToDTO(supplierOpt.get());
        return ResponseEntity.ok(supplierDTO);
    }


    // Create a new supplier (Admin Only)
    @PostMapping
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierDTO supplierDTO) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Long loggedInUserId = getLoggedInUserId();
        Optional<User> postedBy = userRepository.findById(loggedInUserId);

        if (postedBy.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Supplier> existingSupplierOpt = supplierRepository.findBySupplierCode(supplierDTO.getSupplierCode());

        Supplier supplier;
        if (existingSupplierOpt.isPresent()) {
            supplier = existingSupplierOpt.get();

            if ("Y".equals(supplier.getDeletedFlag())) {
                // Restore deleted supplier
                supplier.setDeletedFlag("N");
                supplier.setDeletedBy(null);
                supplier.setDeletedTime(null);
            }

            // Update supplier details
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setContactPerson(supplierDTO.getContactPerson());
            supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
            supplier.setEmail(supplierDTO.getEmailAddress());
            supplier.setPhysicalAddress(supplierDTO.getPhysicalAddress());
            supplier.setPostalAddress(supplierDTO.getPostalAddress());

            // Mark as modified
            supplier.setModifiedBy(postedBy.get());
            supplier.setModifiedFlag("Y");
            supplier.setModifiedTime(LocalDateTime.now());

        } else {
            // Create a new supplier
            supplier = new Supplier(
                    supplierDTO.getSupplierCode(),
                    supplierDTO.getSupplierName(),
                    supplierDTO.getContactPerson(),
                    supplierDTO.getPhoneNumber(),
                    supplierDTO.getEmailAddress(),
                    supplierDTO.getPhysicalAddress(),
                    supplierDTO.getPostalAddress(),
                    postedBy.get()
            );
        }

        return ResponseEntity.ok(supplierRepository.save(supplier));
    }

    // Update an existing supplier (Admin Only)
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO supplierDTO) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        Optional<User> modifiedBy = userRepository.findById(getLoggedInUserId());

        if (existingSupplier.isEmpty() || modifiedBy.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Supplier supplier = existingSupplier.get();
        supplier.setSupplierCode(supplierDTO.getSupplierCode());
        supplier.setSupplierName(supplierDTO.getSupplierName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplier.setEmail(supplierDTO.getEmailAddress());
        supplier.setPhysicalAddress(supplierDTO.getPhysicalAddress());
        supplier.setPostalAddress(supplierDTO.getPostalAddress());

        // Update modification details
        supplier.setModifiedBy(modifiedBy.get());
        supplier.setModifiedFlag("Y");
        supplier.setModifiedTime(LocalDateTime.now());

        return ResponseEntity.ok(supplierRepository.save(supplier));
    }

    // Delete supplier (soft delete) (Admin Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(403).build();
        }

        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        Optional<User> deletedBy = userRepository.findById(getLoggedInUserId());

        if (existingSupplier.isEmpty() || deletedBy.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Supplier supplier = existingSupplier.get();
        supplier.setDeletedBy(deletedBy.get());
        supplier.setDeletedFlag("Y");
        supplier.setDeletedTime(LocalDateTime.now());

        supplierRepository.save(supplier);

        return ResponseEntity.noContent().build();
    }

    // 🔹 Helper method to get the logged-in user's ID
    private Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(loggedInEmail);
        return userOpt.map(User::getId).orElse(null);
    }

    // 🔹 Helper method to check if logged-in user is ADMIN
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(loggedInEmail);
        return userOpt.map(user -> user.getRole() == Role.ADMIN).orElse(false);
    }

    private SupplierResponseDTO convertToDTO(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setId(supplier.getId());
        dto.setSupplierCode(supplier.getSupplierCode());
        dto.setSupplierName(supplier.getSupplierName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setPhoneNumber(supplier.getPhoneNumber());
        dto.setEmailAddress(supplier.getEmail());
        dto.setPhysicalAddress(supplier.getPhysicalAddress());
        dto.setPostalAddress(supplier.getPostalAddress());
        dto.setPostedBy(supplier.getPostedBy() != null ? convertUserToDTO(supplier.getPostedBy()) : null);
        dto.setPostedFlag(supplier.getPostedFlag());
        dto.setPostedTime(supplier.getPostedTime());
        dto.setModifiedBy(supplier.getModifiedBy() != null ? convertUserToDTO(supplier.getModifiedBy()) : null);
        dto.setModifiedTime(supplier.getModifiedTime());
        dto.setDeletedBy(supplier.getDeletedBy() != null ? convertUserToDTO(supplier.getDeletedBy()) : null);
        dto.setDeletedFlag(supplier.getDeletedFlag());
        dto.setDeletedTime(supplier.getDeletedTime());

        return dto;
    }
    private UserResponseDTO convertUserToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        return dto;
    }

}
