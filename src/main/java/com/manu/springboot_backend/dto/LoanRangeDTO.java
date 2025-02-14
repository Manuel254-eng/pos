//package com.manu.springboot_backend.dto;
//
//import com.manu.springboot_backend.model.RepaymentFrequency;
//import lombok.*;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class LoanRangeDTO {
//    private Long id;  // ✅ Add this field
//    private String packageName;
//    private double minAmount;
//    private double maxAmount;
//    private double interestRate;
//    private int minRepaymentPeriod;
//    private int maxRepaymentPeriod;
//    private Integer minCreditScore;
//    private RepaymentFrequency frequency;
//
//    // ✅ Ensure there is a constructor that includes 'id'
//    public LoanRangeDTO(String packageName, double minAmount, double maxAmount, double interestRate,
//                        int minRepaymentPeriod, int maxRepaymentPeriod, Integer minCreditScore, RepaymentFrequency frequency) {
//        this.packageName = packageName;
//        this.minAmount = minAmount;
//        this.maxAmount = maxAmount;
//        this.interestRate = interestRate;
//        this.minRepaymentPeriod = minRepaymentPeriod;
//        this.maxRepaymentPeriod = maxRepaymentPeriod;
//        this.minCreditScore = minCreditScore;
//        this.frequency = frequency;
//    }
//}
