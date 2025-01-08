package com.payments.payments_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.payments_back.exceptions.BinlistNotFoundException;
import com.payments.payments_back.model.CardRequest;
import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.CardInfoService;
import com.payments.payments_back.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class CardCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardInfoService cardInfoService;

    @MockBean
    private ClearingCostService clearingCostService;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize services and mock behavior
        when(cardInfoService.getCardCountry("371240")).thenReturn("US");
        when(clearingCostService.getClearingCost("US")).thenReturn(new ClearingCost("US", new BigDecimal(1.5)));
    }

    @Test
    public void testGetCardCost_ValidRequest() throws Exception {
        // Create a valid CardRequest
        CardRequest validCardRequest = new CardRequest("371240328956");

        // Perform the POST request
        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("US"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value(1.5));

        // Verify interactions with the mocks
        verify(cardInfoService).getCardCountry("371240");
        verify(clearingCostService).getClearingCost("US");
    }

    @Test
    public void testGetCardCost_InvalidCardNumberLength() throws Exception {
        // Create an invalid CardRequest (too short card number)
        CardRequest invalidCardRequest = new CardRequest("123456");

        // Perform the POST request with invalid data
        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetCardCost_CountryNotFound() throws Exception {
        // Simulate a BinlistNotFoundException
        when(cardInfoService.getCardCountry("123456")).thenThrow(new RuntimeException(new BinlistNotFoundException("Country not found")));

        // Create a valid CardRequest
        CardRequest validCardRequest = new CardRequest("1234567890");

        // Perform the POST request
        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetCardCost_InternalServerError() throws Exception {
        // Simulate an unexpected error
        when(cardInfoService.getCardCountry("123456")).thenThrow(new RuntimeException("Internal error"));

        // Create a valid CardRequest
        CardRequest validCardRequest = new CardRequest("1234567890");

        // Perform the POST request
        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}