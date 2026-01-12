package com.banking.controller;

import com.banking.model.Loan;
import com.banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping
    public Loan applyLoan(@RequestBody Map<String, Object> payload) {
        String accountNumber = (String) payload.get("accountNumber");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return loanService.applyLoan(accountNumber, amount);
    }

    @GetMapping("/{accountNumber}")
    public List<Loan> getMyLoans(@PathVariable String accountNumber) {
        return loanService.getLoansByAccount(accountNumber);
    }
}

