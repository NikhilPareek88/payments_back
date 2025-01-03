package com.payments.payments_back.controller;


import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.ClearingCostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/clearing-cost")
public class ClearingCostController {

    private final ClearingCostService clearingCostService;

    public ClearingCostController(ClearingCostService clearingCostService) {
        this.clearingCostService = clearingCostService;
    }

    @GetMapping("/")
    public ResponseEntity<Collection<ClearingCost>> clearingCost() {
        try {
            ConcurrentHashMap<String, ClearingCost> concurrentHashMap = clearingCostService.getAllClearingCosts();
            return new ResponseEntity<>(concurrentHashMap.values(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<ClearingCost> getClearingCost(@PathVariable("country") String country) {
        try {
            // Here country = OTHER refers for all other countries, so better to return OTHER in case country not found.
            ClearingCost clearingCost = clearingCostService.getClearingCost(country);
            return new ResponseEntity<>(clearingCost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ClearingCost> addClearingCost(@RequestBody ClearingCost clearingCost) {
        try {
            ClearingCost clearingCostRes = clearingCostService.addClearingCost(clearingCost);
            return new ResponseEntity<>(clearingCostRes, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<ClearingCost> removeClearingCost(@RequestBody ClearingCost clearingCost) {
        try {
            clearingCostService.deleteClearingCost(clearingCost.country());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
