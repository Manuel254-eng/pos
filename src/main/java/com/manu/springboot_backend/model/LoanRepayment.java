package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_repayments")
public class LoanRepayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_request_id", nullable = false)
    private LoanRequest loanRequest; // Links to the loan

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The borrower

    private LocalDate repaymentMadeOn; // When repayment was made

    @Column(nullable = false)
    private double installmentAmount; // Expected amount

    private double amountPaid = 0.0; // Amount paid

    private double remainingBalance; // Remaining loan balance

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status = RepaymentStatus.PENDING; // Default status

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // Automatically set when created
}
