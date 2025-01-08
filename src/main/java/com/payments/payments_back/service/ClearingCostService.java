package com.payments.payments_back.service;

import com.payments.payments_back.exceptions.ResourceNotFoundException;
import com.payments.payments_back.model.ClearingCost;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClearingCostService {

    public static final String COUNTRY_US = "US";
    public static final String COUNTRY_GR = "GR";
    public static final String COUNTRY_OTHER = "OTHER";

    private final ConcurrentHashMap<String, ClearingCost> clearingCostMap;

    public ClearingCostService() {
        clearingCostMap = new ConcurrentHashMap<>();
        clearingCostMap.put(COUNTRY_US, new ClearingCost(COUNTRY_US, BigDecimal.valueOf(5.00)));
        clearingCostMap.put(COUNTRY_GR, new ClearingCost(COUNTRY_GR, BigDecimal.valueOf(15.00)));
        clearingCostMap.put(COUNTRY_OTHER, new ClearingCost(COUNTRY_OTHER, BigDecimal.valueOf(10.00)));
    }

    public ClearingCost getClearingCost(String countryCode) {
        return clearingCostMap.getOrDefault(countryCode, clearingCostMap.get("OTHER"));
    }

    public ClearingCost getClearingCostByCountry(String countryCode) {
        if (clearingCostMap.containsKey(countryCode)) {
            return clearingCostMap.get(countryCode);
        } else {
            throw new RuntimeException("Country code " + countryCode + " not found");
        }
    }

    public ClearingCost addClearingCost(ClearingCost clearingCost) {
        clearingCostMap.put(clearingCost.country(), clearingCost);
        return clearingCost;
    }

    public void deleteClearingCost(String countryCode) {
        if (clearingCostMap.containsKey(countryCode)) {
            clearingCostMap.remove(countryCode);
        } else {
            throw new ResourceNotFoundException("Country code " + countryCode + " not found");
        }
    }

    public ConcurrentHashMap<String, ClearingCost> getAllClearingCosts() {
        return new ConcurrentHashMap<>(clearingCostMap);
    }
}
