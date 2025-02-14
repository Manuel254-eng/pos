package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.LoanRepayment;
import com.manu.springboot_backend.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {

    // ✅ Find all repayments for a specific loan request (Payment History)
    List<LoanRepayment> findByLoanRequest(LoanRequest loanRequest);

    // ✅ Find the latest repayment for a specific loan request

    Optional<LoanRepayment> findTopByLoanRequestIdOrderByCreatedAtDesc(Long loanRequestId);

    List<LoanRepayment> findByLoanRequestId(Long id);
}
