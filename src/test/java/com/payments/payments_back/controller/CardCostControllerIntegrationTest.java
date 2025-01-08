package com.payments.payments_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.payments_back.model.CardRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class CardCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCardCost_Success() throws Exception {
        CardRequest validCardRequest = new CardRequest("457173608989"); // Example card number

        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCardRequest)))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.country").value("DK"))
                .andExpect(jsonPath("$.cost").value(10.00));
    }

    @Test
    void testGetCardCost_InvalidCardNumberLength() throws Exception {
        CardRequest invalidCardRequest = new CardRequest("12345");

        // Perform the POST request
        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCardRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCardCost_CountryNotFound() throws Exception {
        CardRequest validCardRequest = new CardRequest("1234567890");

        mockMvc.perform(post("/payment-cards-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCardRequest)))
                .andExpect(status().isNotFound());
    }
}