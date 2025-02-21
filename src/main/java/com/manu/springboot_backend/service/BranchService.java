package com.manu.springboot_backend.service;


import com.manu.springboot_backend.dto.BranchDTO;
import com.manu.springboot_backend.model.Branch;
import com.manu.springboot_backend.repository.BranchRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    public Branch createBranch(BranchDTO branchDTO) {
        Branch branch = new Branch();
        branch.setName(branchDTO.getName());
        branch.setLocation(branchDTO.getLocation());
        return branchRepository.save(branch);
    }

    public boolean deleteBranch(Long id) {
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Branch> updateBranch(Long id, BranchDTO branchDTO) {
        return branchRepository.findById(id).map(existingBranch -> {
            // Update only the fields that are provided in the request
            if (branchDTO.getName() != null) {
                existingBranch.setName(branchDTO.getName());
            }
            if (branchDTO.getLocation() != null) {
                existingBranch.setLocation(branchDTO.getLocation());
            }

            return branchRepository.save(existingBranch);
        });
    }
}