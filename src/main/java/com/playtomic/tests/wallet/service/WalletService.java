package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.TransactionDTO;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.mapper.TransactionMapper;
import com.playtomic.tests.wallet.respository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    private final TransactionMapper transactionMapper;

    public WalletService(final WalletRepository walletRepository, TransactionMapper transactionMapper) {
        this.walletRepository = walletRepository;
        this.transactionMapper = transactionMapper;
    }

    public Optional<Wallet> getWalletByID(String walletId) {
        return walletRepository.findByWalletIdentifier(walletId);
    }

    public void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);

    }

    public Wallet saveWallet(Wallet wallet, TransactionDTO transaction) {
        Transaction trEntity = transactionMapper.mapper(transaction);
        wallet.setCurrentBalance(wallet.getCurrentBalance().add(transaction.getAmount()));
        wallet.getTransactions().add(trEntity);
        return walletRepository.save(wallet);
    }
}
