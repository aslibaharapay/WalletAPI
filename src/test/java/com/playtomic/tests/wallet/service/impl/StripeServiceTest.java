package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeServiceException;
import com.playtomic.tests.wallet.service.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.math.BigDecimal;
import java.net.URI;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
 class StripeServiceTest {

    URI testUri = URI.create("https://sandbox.playtomic.io/v1/stripe-simulator/charges");
    StripeService s = new StripeService(testUri, testUri, new RestTemplateBuilder());

    @Test
    void test_exception() {
        BigDecimal price = new BigDecimal(5);
        Assertions.assertThrows(StripeAmountTooSmallException.class,
                () -> s.charge("4242 4242 4242 4242", price));
    }

    @Test
    void charge_validcreditcard_success() throws StripeServiceException {

        Assertions.assertDoesNotThrow(() -> s.charge("4242 4242 4242 4242", new BigDecimal(15)));
    }
}
