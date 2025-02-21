package com.manu.springboot_backend.controller;

import com.manu.springboot_backend.dto.BranchDTO;
import com.manu.springboot_backend.model.Branch;
import com.manu.springboot_backend.service.BranchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBranches() {
        List<Branch> branches = branchService.getAllBranches();
        return ResponseEntity.ok(Map.of(
                "message", "Branches retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", branches
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBranchById(@PathVariable Long id) {
        Optional<Branch> branch = branchService.getBranchById(id);
        return branch.map(value -> ResponseEntity.ok(Map.of(
                "message", "Branch retrieved successfully",
                "status", HttpStatus.OK.value(),
                "data", value
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Branch not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBranch(@RequestBody BranchDTO branchDTO) {
        Branch savedBranch = branchService.createBranch(branchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Branch created successfully",
                "status", HttpStatus.CREATED.value(),
                "data", savedBranch
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBranch(@PathVariable Long id) {
        boolean deleted = branchService.deleteBranch(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of(
                    "message", "Branch deleted successfully",
                    "status", HttpStatus.OK.value()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Branch not found",
                    "status", HttpStatus.NOT_FOUND.value()
            ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBranch(@PathVariable Long id, @RequestBody BranchDTO branchDTO) {
        Optional<Branch> updatedBranch = branchService.updateBranch(id, branchDTO);

        return updatedBranch.map(branch -> ResponseEntity.ok(Map.of(
                "message", "Branch updated successfully",
                "status", HttpStatus.OK.value(),
                "data", branch
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Branch not found",
                "status", HttpStatus.NOT_FOUND.value()
        )));
    }

}
