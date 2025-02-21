package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.SaleOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderLineRepository extends JpaRepository<SaleOrderLine, Long> {
}
