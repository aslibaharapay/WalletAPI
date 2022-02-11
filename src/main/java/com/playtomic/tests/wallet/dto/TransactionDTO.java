package com.playtomic.tests.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {

    private BigDecimal amount;

    private String creditCardNumber;

    private String walletId;
}
