package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByName(String name);
}
