package com.playtomic.tests.wallet.mapper;

import com.playtomic.tests.wallet.dto.TransactionDTO;
import com.playtomic.tests.wallet.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction mapper(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setWalletId(dto.getWalletId());
        transaction.setCreditCardNumber(dto.getCreditCardNumber());
        return transaction;
    }

}
