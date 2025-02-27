package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
    Optional<SaleOrder> findTopByOrderByIdDesc();

}
