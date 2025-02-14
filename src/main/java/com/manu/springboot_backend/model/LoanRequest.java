package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loan_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Person requesting the loan

    private double amount; // Loan amount requested

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.PENDING; // Default status is PENDING

    private int userCreditScore; // User's credit score at the time of request

    private int minCreditScoreRequired; // Minimum score needed for approval

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver; // Admin who approves/rejects (nullable)

    private LocalDateTime requestDate = LocalDateTime.now();

    // ✅ New Columns
    private double interestRate;
   // add the interest rate frequency per annum or per month or per week or per day needs to be an enum
   @Enumerated(EnumType.STRING)
   private InterestRateFrequency interestRateFrequency; // Enum for frequency

    @Column(nullable = false)
    private double expectedPaymentAmount;


    private int repaymentPeriod;    // Repayment period in months
    private String frequency;       // Repayment frequency (e.g., "Monthly", "Weekly")

}
