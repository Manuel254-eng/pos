package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.LoanRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRangeRepository extends JpaRepository<LoanRange, Long> {
    Optional<LoanRange> findByMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(double minAmount, double maxAmount);
}
