package com.manu.springboot_backend.service;

import com.manu.springboot_backend.dto.LoanRequestResponseDTO;
import com.manu.springboot_backend.model.LoanRepayment;
import com.manu.springboot_backend.model.LoanRequest;
import com.manu.springboot_backend.repository.LoanRepaymentRepository;
import com.manu.springboot_backend.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRepaymentRepository loanRepaymentRepository;

    @Autowired
    public LoanRequestService(LoanRequestRepository loanRequestRepository, LoanRepaymentRepository loanRepaymentRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRepaymentRepository = loanRepaymentRepository;
    }

    // ✅ Service method to fetch all fully paid loans
    public List<LoanRequestResponseDTO> getPaidLoans() {
        return loanRequestRepository.findAll().stream()
                .filter(loan -> {
                    List<LoanRepayment> repayments = loanRepaymentRepository.findByLoanRequestId(loan.getId());
                    double totalPaid = repayments.stream().mapToDouble(LoanRepayment::getAmountPaid).sum();
                    double remainingBalance = loan.getExpectedPaymentAmount() - totalPaid;
                    return remainingBalance <= 0; // Fully paid loans
                })
                .map(LoanRequestResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ Service method to fetch all unpaid loans
    public List<LoanRequestResponseDTO> getUnpaidLoans() {
        return loanRequestRepository.findAll().stream()
                .filter(loan -> {
                    List<LoanRepayment> repayments = loanRepaymentRepository.findByLoanRequestId(loan.getId());
                    double totalPaid = repayments.stream().mapToDouble(LoanRepayment::getAmountPaid).sum();
                    double remainingBalance = loan.getExpectedPaymentAmount() - totalPaid;
                    return remainingBalance > 0; // Unpaid loans
                })
                .map(LoanRequestResponseDTO::new)
                .collect(Collectors.toList());
    }
}
