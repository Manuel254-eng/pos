package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.LoanRequest;
import com.manu.springboot_backend.model.LoanStatus;
import com.manu.springboot_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {

    // Get all loan requests by a specific user
    List<LoanRequest> findByUser(User user);

    // Get all loan requests by status (e.g., PENDING, APPROVED, REJECTED)
    List<LoanRequest> findByStatus(LoanStatus status);

    List<LoanRequest> findByUserId(Long userId);

    boolean existsByUserAndStatus(User user, LoanStatus status);

    @Query("SELECT lr FROM LoanRequest lr WHERE " +
            "(SELECT COALESCE(SUM(r.amountPaid), 0) FROM LoanRepayment r WHERE r.loanRequest = lr) >= lr.amount")
    List<LoanRequest> findAllPaidLoans();

}
