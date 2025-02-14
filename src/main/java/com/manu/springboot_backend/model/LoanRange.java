package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "loan_ranges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// this particular model is used to create loan packages with different loan amounts, interest rates, and repayment periods
public class LoanRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String packageName; // Package name
    private double minAmount; // Minimum loan amount
    private double maxAmount; // Maximum loan amount
    private double interestRate; // Interest rate (percentage per annum)

    private int minRepaymentPeriod; // Minimum repayment period (in months)
    private int maxRepaymentPeriod; // Maximum repayment period (in months)

    @Column(nullable = false)
    private Integer minCreditScore;

    @Enumerated(EnumType.STRING)
    private RepaymentFrequency frequency; // Repayment frequency (WEEKLY, MONTHLY, YEARLY)



}
