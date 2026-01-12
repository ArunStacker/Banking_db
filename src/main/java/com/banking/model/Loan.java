package com.banking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private BigDecimal amount;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDate applicationDate;

    @PrePersist
    protected void onCreate() {
        applicationDate = LocalDate.now();
        if (status == null) {
            status = "PENDING";
        }
    }


}
