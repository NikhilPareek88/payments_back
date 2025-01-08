package com.payments.payments_back.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.payments.payments_back.exceptions.ResourceNotFoundException;
import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@WebMvcTest(ClearingCostController.class)
class ClearingCostControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClearingCostService clearingCostService;

    private ClearingCost validClearingCost;

    @BeforeEach
    void setUp() {
        validClearingCost = new ClearingCost("US", new BigDecimal("1.5"));
    }

    @TestConfiguration
    static class MockServiceConfiguration {

        @Bean
        public ClearingCostService clearingCostService() {
            ClearingCostService mockService = mock(ClearingCostService.class);
            ConcurrentHashMap<String, ClearingCost> clearingCosts = new ConcurrentHashMap<>();
            clearingCosts.put("US", new ClearingCost("US", new BigDecimal("1.5")));
            when(mockService.getAllClearingCosts()).thenReturn(clearingCosts);
            when(mockService.getClearingCost("US")).thenReturn(new ClearingCost("US", new BigDecimal("1.5")));
            when(mockService.addClearingCost(any(ClearingCost.class))).thenReturn(new ClearingCost("US", new BigDecimal("1.5")));
            doNothing().when(mockService).deleteClearingCost(anyString());
            return mockService;
        }
    }

    @Test
    void testClearingCost() throws Exception {
        // Simulate service response
        when(clearingCostService.getAllClearingCosts()).thenReturn(new ConcurrentHashMap<String, ClearingCost>() {{
            put("US", validClearingCost);
        }});

        mockMvc.perform(get("/clearing-cost/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].country").value("US"))
                .andExpect(jsonPath("$.[0].cost").value(1.5));
    }

    @Test
    void testGetClearingCostSuccess() throws Exception {
        // Simulate service response
        when(clearingCostService.getClearingCost("US")).thenReturn(validClearingCost);

        mockMvc.perform(get("/clearing-cost/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("US"))
                .andExpect(jsonPath("$.cost").value(1.5));
    }

    @Test
    void testAddClearingCost() throws Exception {
        // Simulate service response
        when(clearingCostService.addClearingCost(validClearingCost)).thenReturn(validClearingCost);

        mockMvc.perform(post("/clearing-cost/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"country\": \"US\", \"cost\": 1.5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country").value("US"))
                .andExpect(jsonPath("$.cost").value(1.5));
    }

    @Test
    void testResourceNotFoundExceptiorWhileRemoving() throws Exception {
        doThrow(ResourceNotFoundException.class).when(clearingCostService).deleteClearingCost("XS");

        mockMvc.perform(delete("/clearing-cost/country/XS"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveClearingCost() throws Exception {
        // Simulate service response
        doNothing().when(clearingCostService).deleteClearingCost("US");

        mockMvc.perform(delete("/clearing-cost/country/US"))
                .andExpect(status().isOk());
    }
}