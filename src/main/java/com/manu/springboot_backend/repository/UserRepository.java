package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.Role;
import com.manu.springboot_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ✅ Fetch only customers
    List<User> findByRole(Role role);
}
