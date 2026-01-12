package com.banking.service;

import com.banking.model.Account;
import com.banking.model.Loan;
import com.banking.repository.AccountRepository;
import com.banking.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Loan applyLoan(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Loan loan = new Loan();
        loan.setAccount(account);
        loan.setAmount(amount);
        // Status set to PENDING by @PrePersist logic in Entity
        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return loanRepository.findByAccountId(account.getId());
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Transactional
    public Loan updateLoanStatus(Long loanId, String status) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        loan.setStatus(status); // APPROVED, REJECTED
        return loanRepository.save(loan);
    }
}
