package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class WalletConcurrentTopUpMoneyTest {

	//TESTS ARE NOT GUARANTEED TO WORK BECAUSE OF THREADS

	@Autowired
	WalletService walletService;

	@Autowired
	StripeService stripeService;

	@Autowired
	MockMvc mockMvc;

	private final String WALLET_ID = UUID.randomUUID().toString();

	ObjectMapper om;

	@BeforeAll
	public void setup() throws URISyntaxException {
		om = new ObjectMapper();
		ReflectionTestUtils.setField(stripeService,
				"chargesUri",
				new URI("https://sandbox.playtomic.io/v1/stripe-simulator/charges"));

		Wallet wallet = new Wallet();
		wallet.setWalletIdentifier(WALLET_ID);
		wallet.setCurrentBalance(BigDecimal.ZERO);
		walletService.saveWallet(wallet);
	}

	@Test
	public void test_topUpMoneyInWallet_Status_FOUND1() throws Exception {
		System.out.println("SecondParallelUnitTest first() start => " + Thread.currentThread().getName());

		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(30));
		transaction.setWalletId(WALLET_ID);
		transaction.setCreditCardNumber("4242 4242 4242 4242");

		mockMvc.perform(put("/wallet/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(transaction)))
				.andDo(print())
				.andExpect(status().isOk());


	}
	@Test
	public void test_topUpMoneyInWallet_Status_FOUND2() throws Exception {
		System.out.println("SecondParallelUnitTest second() start => " + Thread.currentThread().getName());
		Thread.sleep(500);
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(70));
		transaction.setWalletId(WALLET_ID);
		transaction.setCreditCardNumber("4242 4242 4242 4242");

		mockMvc.perform(put("/wallet/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(transaction)))
				.andDo(print())
				.andExpect(status().isLocked());

	}

}
