package com.playtomic.tests.wallet.entity;

import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "transactions")
@EnableJpaAuditing
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;

    private String creditCardNumber;

    @Column(name = "wallet_id")
    private String walletId;

}

