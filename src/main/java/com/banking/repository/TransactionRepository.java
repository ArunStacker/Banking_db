package com.banking.repository;

import com.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdAndTimestampBetween(Long accountId, LocalDateTime start, LocalDateTime end);

    List<Transaction> findByAccountId(Long accountId);
}