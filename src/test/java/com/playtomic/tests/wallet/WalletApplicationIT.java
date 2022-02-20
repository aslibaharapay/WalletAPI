package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WalletApplicationIT {

	@Autowired
	WalletService walletService;

	@Autowired
	StripeService stripeService;

	@Autowired
	MockMvc mockMvc;

	private final String WALLET_ID = UUID.randomUUID().toString();

	ObjectMapper om;

	@BeforeAll
	void setup() throws URISyntaxException {
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
	void test_getWalletByID_Status_OK() throws Exception {

		Wallet actualRecords = om.readValue(mockMvc.perform(get("/wallet/"+WALLET_ID))
				.andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Wallet.class);

		Optional<Wallet> expectedWallet = walletService.getWalletByID(WALLET_ID);
		assertTrue(expectedWallet.isPresent());
		assertNotNull(actualRecords);
		assertAll("actualRecords",
				() -> assertEquals(actualRecords.getCurrentBalance(), expectedWallet.get().getCurrentBalance()),
				() -> assertEquals(actualRecords.getWalletIdentifier(), expectedWallet.get().getWalletIdentifier()),
				() -> assertEquals(actualRecords.getId(), expectedWallet.get().getId())
		);
	}

	@Test
	void test_getWalletByInValidID_Status_NOTFOUND() throws Exception {

		mockMvc.perform(get("/wallet/"+"NOT_VALID_ID"))
				.andDo(print())
				.andExpect(status().isNotFound());
	}

	@Test
	void test_WalletService_NotAllowedMethod() throws Exception {

		mockMvc.perform(put("/wallet/" + WALLET_ID))
				.andExpect(status().isMethodNotAllowed());

		mockMvc.perform(patch("/wallet/" + WALLET_ID))
				.andExpect(status().isMethodNotAllowed());

		mockMvc.perform(delete("/wallet/" + WALLET_ID))
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	void test_topUpMoneyInWallet_Status_Not_Found() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(30));
		transaction.setWalletId("NOT_VALID_ID");
		transaction.setCreditCardNumber("4242 4242 4242 4242");

		mockMvc.perform(put("/wallet/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(transaction)))
				.andDo(print())
				.andExpect(status().isNotFound());

	}

	@Test
	void test_topUpMoneyInWallet_Status_FOUND() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(30));
		transaction.setWalletId(WALLET_ID);
		transaction.setCreditCardNumber("4242 4242 4242 4242");

		Wallet actualWallet = om.readValue(mockMvc.perform(put("/wallet/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(transaction)))
				.andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), Wallet.class);

		Optional<Wallet> expectedWallet = walletService.getWalletByID(WALLET_ID);
		assertTrue(expectedWallet.isPresent());

		assertNotNull(actualWallet);
		assertAll("actualWallet",
				() -> assertEquals(actualWallet.getCurrentBalance(), expectedWallet.get().getCurrentBalance()),
				() -> assertEquals(actualWallet.getWalletIdentifier(),  expectedWallet.get().getWalletIdentifier()),
				() -> assertEquals(actualWallet.getTransactions().size(),expectedWallet.get().getTransactions().size())
		);
	}



}
