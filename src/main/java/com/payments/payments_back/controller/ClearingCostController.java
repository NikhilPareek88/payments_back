package com.payments.payments_back.controller;


import com.payments.payments_back.exceptions.ResourceNotFoundException;
import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.ClearingCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get Countries & Clearing Cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/")
    public ResponseEntity<Collection<ClearingCost>> clearingCost() {
        try {
            ConcurrentHashMap<String, ClearingCost> concurrentHashMap = clearingCostService.getAllClearingCosts();
            return new ResponseEntity<>(concurrentHashMap.values(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Clearing Cost Given on Country")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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

    @Operation(summary = "Create a new Country & Clearing Cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/")
    public ResponseEntity<ClearingCost> addClearingCost(@RequestBody ClearingCost clearingCost) {
        try {
            ClearingCost clearingCostRes = clearingCostService.addClearingCost(clearingCost);
            return new ResponseEntity<>(clearingCostRes, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete Country & Clearing Cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/country/{country}")
    public ResponseEntity<ClearingCost> removeClearingCost(@PathVariable String country) {
        try {
            clearingCostService.deleteClearingCost(country);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
