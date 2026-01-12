package com.banking.service;

import com.banking.model.Account;

import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Account createAccount(User user, BigDecimal initialDeposit) {
        if (initialDeposit.compareTo(new BigDecimal("500")) < 0) {
            throw new RuntimeException("Minimum balance of 500 is required to open an account.");
        }

        // Check if user exists by email, else save
        // Simplified: Always saving new user or expecting user details in payload
        // Ideally we check if email exists.
        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(initialDeposit);
        account.setAccountType("SAVINGS"); // Default
        account.setAccountNumber(generateAccountNumber());

        Account savedAccount = accountRepository.save(account);

        // Initial Transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(savedAccount);
        transaction.setAmount(initialDeposit);
        transaction.setType("DEPOSIT");
        transactionRepository.save(transaction);

        return savedAccount;
    }

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(savedAccount);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transactionRepository.save(transaction);

        return savedAccount;
    }

    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account savedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(savedAccount);
        transaction.setAmount(amount.negate());
        transaction.setType("WITHDRAW");
        transactionRepository.save(transaction);

        return savedAccount;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> searchAccounts(String accountNumberFragment) {
        return accountRepository.findByAccountNumberContaining(accountNumberFragment);
    }

    public List<Transaction> getStatement(String accountNumber, LocalDateTime start, LocalDateTime end) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccountIdAndTimestampBetween(account.getId(), start, end);
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
