package com.payments.payments_back.service;

import com.payments.payments_back.exceptions.ResourceNotFoundException;
import com.payments.payments_back.model.ClearingCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ClearingCostServiceTest {

    private ClearingCostService clearingCostService;

    @BeforeEach
    void setUp() {
        clearingCostService = new ClearingCostService();
    }

    @Test
    void testGetClearingCost_ValidCountry() {
        ClearingCost usClearingCost = clearingCostService.getClearingCost(ClearingCostService.COUNTRY_US);
        assertNotNull(usClearingCost);
        assertEquals("US", usClearingCost.country());
        assertEquals(BigDecimal.valueOf(5.00), usClearingCost.cost());

        ClearingCost grClearingCost = clearingCostService.getClearingCost(ClearingCostService.COUNTRY_GR);
        assertNotNull(grClearingCost);
        assertEquals("GR", grClearingCost.country());
        assertEquals(BigDecimal.valueOf(15.00), grClearingCost.cost());
    }

    @Test
    void testGetClearingCost_InvalidCountry() {
        ClearingCost otherClearingCost = clearingCostService.getClearingCost("INVALID_COUNTRY");
        assertNotNull(otherClearingCost);
        assertEquals(ClearingCostService.COUNTRY_OTHER, otherClearingCost.country());
        assertEquals(BigDecimal.valueOf(10.00), otherClearingCost.cost());
    }

    @Test
    void testGetClearingCostByCountry_ValidCountry() {
        ClearingCost usClearingCost = clearingCostService.getClearingCostByCountry(ClearingCostService.COUNTRY_US);
        assertNotNull(usClearingCost);
        assertEquals("US", usClearingCost.country());
        assertEquals(BigDecimal.valueOf(5.00), usClearingCost.cost());
    }

    @Test
    void testGetClearingCostByCountry_InvalidCountry() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clearingCostService.getClearingCostByCountry("INVALID_COUNTRY");
        });
        assertEquals("Country code INVALID_COUNTRY not found", exception.getMessage());
    }

    @Test
    void testAddClearingCost() {
        ClearingCost newClearingCost = new ClearingCost("IN", BigDecimal.valueOf(20.00));
        ClearingCost addedCost = clearingCostService.addClearingCost(newClearingCost);

        assertNotNull(addedCost);
        assertEquals("IN", addedCost.country());
        assertEquals(BigDecimal.valueOf(20.00), addedCost.cost());

        // Ensure that the new country is added to the map
        ClearingCost fetchedCost = clearingCostService.getClearingCostByCountry("IN");
        assertEquals(addedCost, fetchedCost);
    }

    @Test
    void testDeleteClearingCost_Success() {
        clearingCostService.deleteClearingCost(ClearingCostService.COUNTRY_US);

        // Ensure the country is removed
        Exception exception = assertThrows(RuntimeException.class, () -> {
            clearingCostService.getClearingCostByCountry(ClearingCostService.COUNTRY_US);
        });
        assertEquals("Country code US not found", exception.getMessage());
    }

    @Test
    void testDeleteClearingCost_NotFound() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clearingCostService.deleteClearingCost("INVALID_COUNTRY");
        });
        assertEquals("Country code INVALID_COUNTRY not found", exception.getMessage());
    }

    @Test
    void testGetAllClearingCosts() {
        ConcurrentHashMap<String, ClearingCost> clearingCosts = clearingCostService.getAllClearingCosts();

        assertNotNull(clearingCosts);
        assertTrue(clearingCosts.containsKey(ClearingCostService.COUNTRY_US));
        assertTrue(clearingCosts.containsKey(ClearingCostService.COUNTRY_GR));
        assertTrue(clearingCosts.containsKey(ClearingCostService.COUNTRY_OTHER));
    }
}
