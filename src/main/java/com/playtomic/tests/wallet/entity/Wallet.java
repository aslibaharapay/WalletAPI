package com.playtomic.tests.wallet.entity;

import com.playtomic.tests.wallet.validation.BalanceCheck;
import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "wallet_identifier")
    private String walletIdentifier;

    @NotNull
    @ColumnDefault("0.00")
    @BalanceCheck
    private BigDecimal currentBalance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_identifier")
    private Set<Transaction> transactions = new HashSet<>();

    @Version
    private long version;
}
