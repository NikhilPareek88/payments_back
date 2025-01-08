package com.payments.payments_back.controller;

import com.payments.payments_back.model.CardCostResponse;
import com.payments.payments_back.model.CardRequest;
import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.CardInfoService;
import com.payments.payments_back.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CardCostControllerTest {

    @Mock
    private CardInfoService cardInfoService;

    @Mock
    private ClearingCostService clearingCostService;

    @InjectMocks
    private CardCostController cardCostController;

    private CardRequest validCardRequest;
    private CardRequest invalidCardRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up valid and invalid card requests for testing
        validCardRequest = new CardRequest("1234567890123456");  // valid card number
        invalidCardRequest = new CardRequest("123");  // invalid card number (too short)
    }

    @Test
    void testGetCardCostSuccess() throws Exception {
        // Prepare mock behavior for the services
        String mockCountry = "US";
        ClearingCost mockClearingCost = new ClearingCost("US", new BigDecimal(1.5));  // Assume cost is 1.5
        when(cardInfoService.getCardCountry("123456")).thenReturn(mockCountry);
        when(clearingCostService.getClearingCost(mockCountry)).thenReturn(mockClearingCost);

        // Call the method under test
        ResponseEntity<CardCostResponse> response = cardCostController.getCardCost(validCardRequest);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(mockCountry, response.getBody().country());
        assertEquals(new BigDecimal(1.5), response.getBody().cost());
    }

    @Test
    void testGetCardCostInvalidCardNumber() {
        // Call the method under test with invalid card number
        ResponseEntity<CardCostResponse> response = cardCostController.getCardCost(invalidCardRequest);

        // Assertions
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testGetCardCostInternalServerError() throws Exception {
        // Simulate an exception in the service
        when(cardInfoService.getCardCountry(anyString())).thenThrow(new RuntimeException("Service error"));

        // Call the method under test
        ResponseEntity<CardCostResponse> response = cardCostController.getCardCost(validCardRequest);

        // Assertions
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testGetCardCostBadRequestDueToEmptyCardNumber() {
        CardRequest emptyCardRequest = new CardRequest("");

        ResponseEntity<CardCostResponse> response = cardCostController.getCardCost(emptyCardRequest);

        // Assertions
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
