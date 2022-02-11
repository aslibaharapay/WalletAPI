package com.playtomic.tests.wallet.respository;

import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByWalletIdentifier(String ID);

    Optional<Wallet> findCurrentBalanceByWalletIdentifier(String ID);

}
