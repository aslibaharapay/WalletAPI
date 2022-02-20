package com.playtomic.tests.wallet.controller;

import com.playtomic.tests.wallet.dto.TransactionDTO;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletRestController {
    /*
    For Proof of concept - DB lock is preferable
    Real life scenario too much db lock result in bottleneck so
    REDIS Distributed Locking Tools can be used.
     */

    private final WalletService walletService;

    private final StripeService stripeService;

    public WalletRestController(WalletService walletService, StripeService stripeService) {
        this.walletService = walletService;
        this.stripeService = stripeService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> topUpMoneyInWallet(@RequestBody TransactionDTO transaction) {
        Optional<Wallet> optSelectedWallet = walletService.getWalletByID(transaction.getWalletId());

        if (optSelectedWallet.isPresent()) {
            Wallet selectedWallet = optSelectedWallet.get();
            try {
                stripeService.charge(transaction.getCreditCardNumber(), transaction.getAmount());
                Wallet savedWallet = walletService.saveWallet(selectedWallet, transaction);
                return ResponseEntity.ok().body(savedWallet);
            } catch (OptimisticLockingFailureException e) {
                //put optimistic lock to prevent balance errors
                return ResponseEntity.status(HttpStatus.LOCKED).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }

        } else {
            //no need to add transaction table
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Wallet> findWalletById(@PathVariable("id") String id) {
        Optional<Wallet> wallet = walletService.getWalletByID(id);
        return wallet.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
    }


}