package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.BranchDTO;
import com.manu.springboot_backend.dto.ItemResponseDTO;
import com.manu.springboot_backend.dto.ItemsDTO;
import com.manu.springboot_backend.dto.UserResponseDTO;
import com.manu.springboot_backend.model.*;
import com.manu.springboot_backend.repository.*;
import com.manu.springboot_backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    private final ItemsRepository itemsRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ItemsController(ItemsRepository itemsRepository, UserRepository userRepository, BranchRepository branchRepository, ProductCategoryRepository productCategoryRepository, SupplierRepository supplierRepository) {
        this.itemsRepository = itemsRepository;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.supplierRepository = supplierRepository;
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrUpdateItem(@RequestBody ItemsDTO itemsDTO) {

        // Get the logged-in admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        User loggedInUser = userRepository.findByEmail(loggedInEmail).orElse(null);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin user not found"));
        }

        Long userId = getLoggedInUserId(); // Get the logged-in user's ID

        // Fetch branch by branchId
        Branch branch = branchRepository.findById(itemsDTO.getBranchId()).orElse(null);
        if (branch == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Branch not found"));
        }
        // Fetch branch by branchId
        Supplier supplier = supplierRepository.findById(itemsDTO.getSupplierId()).orElse(null);
        ProductCategory category = (itemsDTO.getCategoryId() != null) ?
                productCategoryRepository.findById(itemsDTO.getCategoryId()).orElse(null) : null;



        Optional<Item> existingItemOpt = itemsRepository.findByName(itemsDTO.getName());

        if (existingItemOpt.isPresent()) {
            System.out.println("Item already exists");
            Item existingItem = existingItemOpt.get();

            if ("Y".equals(existingItem.getDeletedFlag())) {
                // If item was deleted, restore it and update details
                existingItem.setDeletedFlag("N");

                // Update item details
                existingItem.setDescription(itemsDTO.getDescription());
                existingItem.setCount(itemsDTO.getCount());
                existingItem.setModifiedBy(loggedInUser);
                existingItem.setModifiedTime(LocalDateTime.now());

                itemsRepository.save(existingItem);
                return ResponseEntity.ok(Map.of("message", "Item restored and updated successfully", "status", HttpStatus.OK.value()));
            } else  {
                return ResponseEntity.ok(Map.of("message", "Item already exists",  "status", HttpStatus.OK.value()));
            }

        }

        // Create a new item if it doesn't exist
        Item item = new Item();
        item.setName(itemsDTO.getName());
        item.setDescription(itemsDTO.getDescription());
        item.setCount(itemsDTO.getCount());
        item.setPostedBy(loggedInUser);
        item.setPostedTime(LocalDateTime.now());
        item.setPostedFlag("Y");
        item.setBranch(branch);
        item.setSupplier(supplier);
        item.setCategory(category);
        item.setPrice(itemsDTO.getPrice());

        itemsRepository.save(item);
        return ResponseEntity.ok(Map.of("message", "Item added successfully", "status", HttpStatus.CREATED.value()));
    }

    // ✅ Get item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        Optional<Item> itemOpt = itemsRepository.findById(id);

        if (itemOpt.isEmpty() || "Y".equals(itemOpt.get().getDeletedFlag())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Item item = itemOpt.get();
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setCount(item.getCount());
        dto.setPostedFlag(item.getPostedFlag());
        dto.setPostedTime(item.getPostedTime());
        dto.setPrice(item.getPrice());

        // Set the userDTO for postedBy, but exclude password
        UserResponseDTO postedBy = new UserResponseDTO();
        postedBy.setId(item.getPostedBy().getId());
        postedBy.setName(item.getPostedBy().getName());
        postedBy.setEmail(item.getPostedBy().getEmail());
        postedBy.setRole(item.getPostedBy().getRole().toString());

        dto.setPostedBy(postedBy);

        // Set the userDTO for modifiedBy, but exclude password
        if (item.getModifiedBy() != null) {
            UserResponseDTO modifiedBy = new UserResponseDTO();
            modifiedBy.setId(item.getModifiedBy().getId());
            modifiedBy.setName(item.getModifiedBy().getName());
            modifiedBy.setEmail(item.getModifiedBy().getEmail());
            modifiedBy.setRole(item.getModifiedBy().getRole().toString());

            dto.setModifiedBy(modifiedBy);
        }

        // Set the userDTO for deletedBy, but exclude password
        if (item.getDeletedBy() != null) {
            UserResponseDTO deletedBy = new UserResponseDTO();
            deletedBy.setId(item.getDeletedBy().getId());
            deletedBy.setName(item.getDeletedBy().getName());
            deletedBy.setEmail(item.getDeletedBy().getEmail());
            deletedBy.setRole(item.getDeletedBy().getRole().toString());

            dto.setDeletedBy(deletedBy);
        }
        dto.setBranch(item.getBranch());
        dto.setProductCategory(item.getCategory());

        dto.setModifiedTime(item.getModifiedTime());
        dto.setDeletedFlag(item.getDeletedFlag());
        dto.setDeletedTime(item.getDeletedTime());

        return ResponseEntity.ok(dto);
    }


    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        List<Item> items = itemsRepository.findByDeletedFlagNot("Y");
        List<ItemResponseDTO> response = items.stream().map(item -> {
            ItemResponseDTO dto = new ItemResponseDTO();
            dto.setId(item.getId());
            dto.setName(item.getName());
            dto.setDescription(item.getDescription());
            dto.setCount(item.getCount());
            dto.setPrice(item.getPrice());
            dto.setBranch(item.getBranch());
            dto.setProductCategory(item.getCategory());
            dto.setPostedFlag(item.getPostedFlag());
            dto.setPostedTime(item.getPostedTime());

            // Set the userDTO for postedBy, but exclude password
            UserResponseDTO postedBy = new UserResponseDTO();
            postedBy.setId(item.getPostedBy().getId());
            postedBy.setName(item.getPostedBy().getName());
            postedBy.setEmail(item.getPostedBy().getEmail());
            postedBy.setRole(item.getPostedBy().getRole().toString());

            dto.setPostedBy(postedBy);

            // Set the userDTO for modifiedBy, but exclude password
            if (item.getModifiedBy() != null) {
                UserResponseDTO modifiedBy = new UserResponseDTO();
                modifiedBy.setId(item.getModifiedBy().getId());
                modifiedBy.setName(item.getModifiedBy().getName());
                modifiedBy.setEmail(item.getModifiedBy().getEmail());
                modifiedBy.setRole(item.getModifiedBy().getRole().toString());

                dto.setModifiedBy(modifiedBy);
            }

            // Set the userDTO for deletedBy, but exclude password
            if (item.getDeletedBy() != null) {
                UserResponseDTO deletedBy = new UserResponseDTO();
                deletedBy.setId(item.getDeletedBy().getId());
                deletedBy.setName(item.getDeletedBy().getName());
                deletedBy.setEmail(item.getDeletedBy().getEmail());
                deletedBy.setRole(item.getDeletedBy().getRole().toString());

                dto.setDeletedBy(deletedBy);
            }

            dto.setModifiedTime(item.getModifiedTime());
            dto.setDeletedFlag(item.getDeletedFlag());
            dto.setDeletedTime(item.getDeletedTime());

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }




    private Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(loggedInEmail);
        if (userOpt.isPresent()) {
            return userOpt.get().getId(); // Return user ID if found
        }
        return null; // Return null if user not found
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable Long id, @RequestBody ItemsDTO itemsDTO) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        User adminUser = userRepository.findByEmail(loggedInEmail).orElse(null);

        if (adminUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin user not found"));
        }

        Optional<Item> itemOpt = itemsRepository.findById(id);
        if (itemOpt.isEmpty() || "Y".equals(itemOpt.get().getDeletedFlag())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No item with the given ID found"));
        }

        Item item = itemOpt.get();
        item.setName(itemsDTO.getName());
        item.setDescription(itemsDTO.getDescription());
        item.setCount(itemsDTO.getCount());
        item.setModifiedBy(adminUser);
        item.setModifiedFlag("Y");
        item.setModifiedTime(LocalDateTime.now());
        item.setPrice(itemsDTO.getPrice());

        itemsRepository.save(item);

        return ResponseEntity.ok(Map.of("message", "Item updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable Long id) {
        if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Access denied"));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();
        User adminUser = userRepository.findByEmail(loggedInEmail).orElse(null);

        if (adminUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin user not found"));
        }

        Optional<Item> itemOpt = itemsRepository.findById(id);
        if (itemOpt.isEmpty() || "Y".equals(itemOpt.get().getDeletedFlag())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No item with the given ID found"));
        }

        Item item = itemOpt.get();
        item.setDeletedBy(adminUser);
        item.setDeletedTime(LocalDateTime.now());
        item.setDeletedFlag("Y");

        itemsRepository.save(item);

        return ResponseEntity.ok(Map.of("message", "Item deleted successfully"));
    }

    @GetMapping("/count/{name}")
    public ResponseEntity<Map<String, Object>> getItemCountByName(@PathVariable String name) {
        int count = itemsRepository.countByName(name);
        return ResponseEntity.ok(Map.of("itemName", name, "count", count));
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
