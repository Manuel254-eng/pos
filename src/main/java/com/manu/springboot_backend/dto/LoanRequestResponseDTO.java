package com.manu.springboot_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private double amount;
    private String status;
    private double expectedPaymentAmount;
    private int repaymentPeriod;
    private String frequency;
    private double interestRate;
    private String requestDate;

    public LoanRequestResponseDTO(com.manu.springboot_backend.model.LoanRequest loanRequest) {
        this.id = loanRequest.getId();
        this.userId = loanRequest.getUser().getId();
        this.userName = loanRequest.getUser().getName();
        this.amount = loanRequest.getAmount();
        this.status = loanRequest.getStatus().name();
        this.expectedPaymentAmount = loanRequest.getExpectedPaymentAmount();
        this.repaymentPeriod = loanRequest.getRepaymentPeriod();
        this.frequency = loanRequest.getFrequency();
        this.interestRate = loanRequest.getInterestRate();
        this.requestDate = loanRequest.getRequestDate().toString();
    }
}
