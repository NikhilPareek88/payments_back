package com.payments.payments_back.controller;


import com.payments.payments_back.model.ClearingCost;
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

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClearingCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClearingCostService clearingCostService;

    @BeforeEach
    void setUp() {
        // Initialize mock data for clearing costs
        ClearingCost validClearingCost = new ClearingCost("US", new BigDecimal("1.5"));
        ConcurrentHashMap<String, ClearingCost> clearingCosts = new ConcurrentHashMap<>();
        clearingCosts.put("US", validClearingCost);

        // Mock service methods to return expected results
        when(clearingCostService.getAllClearingCosts()).thenReturn(clearingCosts);
        when(clearingCostService.getClearingCost("US")).thenReturn(validClearingCost);
        when(clearingCostService.addClearingCost(any(ClearingCost.class))).thenReturn(validClearingCost);
        doNothing().when(clearingCostService).deleteClearingCost(anyString());
    }

    @Test
    void testClearingCost() throws Exception {
        mockMvc.perform(get("/clearing-cost/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].country").value("US"))
                .andExpect(jsonPath("$.[0].cost").value(1.5));
    }

    @Test
    void testGetClearingCostSuccess() throws Exception {
        mockMvc.perform(get("/clearing-cost/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("US"))
                .andExpect(jsonPath("$.cost").value(1.5));
    }

    @Test
    void testAddClearingCost() throws Exception {
        mockMvc.perform(post("/clearing-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"country\": \"US\", \"cost\": 1.5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country").value("US"))
                .andExpect(jsonPath("$.cost").value(1.5));
    }

    @Test
    void testRemoveClearingCost() throws Exception {
        mockMvc.perform(delete("/clearing-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"country\": \"US\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testInternalServerError() throws Exception {
        when(clearingCostService.getClearingCost("US")).thenThrow(new RuntimeException("Service Error"));

        mockMvc.perform(get("/clearing-cost/country/US"))
                .andExpect(status().isInternalServerError());
    }
}