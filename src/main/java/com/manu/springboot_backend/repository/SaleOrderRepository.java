package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
}
