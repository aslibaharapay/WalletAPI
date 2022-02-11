package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.respository.TransactionRepository;
import com.playtomic.tests.wallet.respository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void adssad() {

    }

}
