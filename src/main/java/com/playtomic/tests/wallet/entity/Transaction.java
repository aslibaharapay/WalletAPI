package com.playtomic.tests.wallet.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Transaction that = (Transaction) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 304080742;
    }
}

