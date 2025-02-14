package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_request_id", nullable = false)
    private LoanRequest loanRequest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate scheduledRepaymentDate;
    private double installmentAmount;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status = RepaymentStatus.PENDING;
}
