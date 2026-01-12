package com.banking.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.banking.service.AccountService;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {



    @Autowired
    private AccountService accountService;

    // Payload: { "name": "...", "email": "...", "phone": "...", "password": "...",
    // "initialDeposit": 500 }
    @PostMapping
    public Account createAccount(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String email = (String) payload.get("email");
        String phone = (String) payload.get("phone");
        String address = (String) payload.get("address");
        String password = (String) payload.get("password");

        // Handle BigDecimal conversion safely
        BigDecimal initialDeposit = new BigDecimal(payload.get("initialDeposit").toString());

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(password);

        return accountService.createAccount(user, initialDeposit);
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(@PathVariable String accountNumber, @RequestBody Map<String, BigDecimal> payload) {
        return accountService.deposit(accountNumber, payload.get("amount"));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(@PathVariable String accountNumber, @RequestBody Map<String, BigDecimal> payload) {
        return accountService.withdraw(accountNumber, payload.get("amount"));
    }

    @GetMapping("/{accountNumber}/statement")
    public List<Transaction> getStatement(
            @PathVariable String accountNumber,
            @RequestParam String startDate, // ISO Date string yyyy-MM-ddTHH:mm:ss
            @RequestParam String endDate) {

        // Simple parser
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        return accountService.getStatement(accountNumber, start, end);
    }
}
