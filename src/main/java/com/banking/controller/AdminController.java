package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.banking.service.AccountService;
import com.banking.service.LoanService;

import com.banking.model.Account;
import com.banking.model.Loan;

import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accounts/search")
    public List<Account> searchAccounts(@RequestParam String accountNumber) {
        return accountService.searchAccounts(accountNumber);
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PutMapping("/loans/{id}")
    public Loan updateLoanStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return loanService.updateLoanStatus(id, payload.get("status"));
    }
}
