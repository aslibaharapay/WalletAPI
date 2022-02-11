package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.TransactionDTO;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.mapper.TransactionMapper;
import com.playtomic.tests.wallet.respository.WalletRepository;
import org.springframework.dao.OptimisticLockingFailureException;
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

    public Optional<Wallet> getWalletByID(String ID) {
        return walletRepository.findByWalletIdentifier(ID);
    }

    public Wallet saveWallet(Wallet wallet) {
        try {
            return walletRepository.save(wallet);
        } catch (OptimisticLockingFailureException ex) {
            throw ex;
        }

    }

    public Wallet saveWallet(Wallet wallet, TransactionDTO transaction) {
        try {
            Transaction trEntitiy = transactionMapper.mapper(transaction);
            wallet.setCurrentBalance(wallet.getCurrentBalance().add(transaction.getAmount()));
            wallet.getTransactions().add(trEntitiy);
            return walletRepository.save(wallet);
        } catch (OptimisticLockingFailureException ex) {
            throw ex;
        }
    }
}
