package com.example.ppay.model;

import com.example.ppay.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Transaction createTransaction(User receiver, Account account) {
        return Transaction.builder()
                .user(receiver)
                .account(account)
                .transactionType(TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
