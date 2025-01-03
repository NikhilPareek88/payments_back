package com.payments.payments_back.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CardInfoServiceIntegrationTest {

    @Autowired
    private CardInfoService cardInfoService;

    @Test
    void testGetCardCountry() throws Exception {
        // A valid 6 digit card number
        String cardNumber = "371240";  // Example: American Express

        // Call the method to get the country
        String country = cardInfoService.getCardCountry(cardNumber);

        // Assert that the country returned is as expected (based on the real BIN number lookup)
        assertNotNull(country, "Country should not be null");
        assertEquals("US", country, "Expected country is US for this card number"); // This will depend on the card number you choose
    }
}
