package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.dto.TransactionDTO;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.mapper.TransactionMapper;
import com.playtomic.tests.wallet.respository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WalletServiceUnitTest {

    private final String WALLET_ID = UUID.randomUUID().toString();

    @Mock
    private WalletRepository walletRepository;

    @Mock
    TransactionMapper transactionMapper;

    @InjectMocks
    private WalletService underTest;

    @Test
    public void test_getWalletByWalletID_successfully() {
        String WALLET_KEY_FOUND = "wallet_key_found";
        Mockito.when(walletRepository.findByWalletIdentifier(WALLET_KEY_FOUND))
                .thenReturn(Optional.of(new Wallet()));
        Optional<Wallet> wallet = underTest.getWalletByID(WALLET_KEY_FOUND);

        assertTrue(wallet.isPresent());
        assertNotNull(wallet.get());

        Mockito.verify(walletRepository, times(1)).findByWalletIdentifier(WALLET_KEY_FOUND);
    }
    @Test
    public void test_getWalletByInvalidkey_ReturnEmtpy() {
        String WALLET_KEY_NOT_FOUND = "wallet_key_not_found";
        Mockito.when(walletRepository.findByWalletIdentifier(WALLET_KEY_NOT_FOUND))
                .thenReturn(Optional.empty());
        Optional<Wallet> wallet = underTest.getWalletByID(WALLET_KEY_NOT_FOUND);

        assertFalse(wallet.isPresent());

        Mockito.verify(walletRepository, times(1)).findByWalletIdentifier(WALLET_KEY_NOT_FOUND);
    }

    @Test
    public void createWallet_ValidDTOGiven_ShouldSuccess() {
        Wallet wallet = new Wallet();
        wallet.setWalletIdentifier(WALLET_ID);

        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);

        underTest.saveWallet(wallet);

        verify(walletRepository, times(1)).save(Mockito.any(Wallet.class));
        assertNotNull(wallet);
    }

    @Test
    public void createWallet_InValidDTOGiven_throwOptimisticLockingFailureException() {
        Wallet wallet = new Wallet();
        wallet.setWalletIdentifier(WALLET_ID);
        wallet.setVersion(2L);
        Mockito.when(walletRepository.save(wallet))
                .thenThrow(new OptimisticLockingFailureException("",null));

        assertThrows(OptimisticLockingFailureException.class ,
                ()->underTest.saveWallet(wallet));
    }


    @Test
    public void createWallet_InValidDTOGiven_throwConstException() {
        Wallet wallet = new Wallet();
        wallet.setWalletIdentifier(WALLET_ID);
        wallet.setCurrentBalance(new BigDecimal(-20));
        Mockito.when(walletRepository.save(wallet))
                .thenThrow(new ConstraintViolationException("",null));

        assertThrows(ConstraintViolationException.class ,
                ()->underTest.saveWallet(wallet));
    }

    @Test
    public void topUpWallet_ValidDTOGiven_ShouldSuccess() {
        Wallet wallet = new Wallet();
        TransactionDTO transaction = new TransactionDTO();
        transaction.setAmount(new BigDecimal(20));
        wallet.setWalletIdentifier(WALLET_ID);
        wallet.setCurrentBalance(new BigDecimal(30));

        Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
        Mockito.when(transactionMapper.mapper(transaction)).thenReturn(new Transaction());

        underTest.saveWallet(wallet,transaction);

        verify(transactionMapper, times(1)).mapper(Mockito.any(TransactionDTO.class));
        verify(walletRepository, times(1)).save(Mockito.any(Wallet.class));

        assertEquals(new BigDecimal(50),wallet.getCurrentBalance()); //20+30
        assertEquals(1, wallet.getTransactions().size());
    }



}
