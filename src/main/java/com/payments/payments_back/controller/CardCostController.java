package com.payments.payments_back.controller;

import com.payments.payments_back.exceptions.BinlistNotFoundException;
import com.payments.payments_back.exceptions.BinlistTooManyException;
import com.payments.payments_back.model.CardCostResponse;
import com.payments.payments_back.model.CardRequest;
import com.payments.payments_back.model.ClearingCost;
import com.payments.payments_back.service.CardInfoService;
import com.payments.payments_back.service.ClearingCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/payment-cards-cost")
@Validated
public class CardCostController {

    private static final Logger logger = LoggerFactory.getLogger(CardCostController.class);

    private final ClearingCostService clearingCostService;
    private final CardInfoService cardInfoService;

    public CardCostController(ClearingCostService clearingCostService, CardInfoService cardInfoService) {
        this.clearingCostService = clearingCostService;
        this.cardInfoService = cardInfoService;
    }

    @Operation(summary = "Clearing cost based on Card number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "It will return the clearing cost for the provided card number"),
            @ApiResponse(responseCode = "400", description = "Bad Request for card number"),
            @ApiResponse(responseCode = "404", description = "IIN doesn't found in BinList Lookup"),
            @ApiResponse(responseCode = "429", description = "BinList response for too many requests"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/")
    public ResponseEntity<CardCostResponse> getCardCost(@Valid @RequestBody CardRequest cardRequest) {
        try {
            logger.debug("Card cost request: {}", cardRequest.cardNumber());

            String iin = getIINFromCardNumber(cardRequest);

            String issuerCountry = cardInfoService.getCardCountry(iin);

            ClearingCost clearingCost = clearingCostService.getClearingCost(issuerCountry);

            return ResponseEntity.ok(new CardCostResponse(issuerCountry, clearingCost.cost()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (e.getCause() instanceof HttpClientErrorException.BadRequest) {
                return ResponseEntity.badRequest().build();
            } else if (e.getCause() instanceof BinlistNotFoundException) {
                return ResponseEntity.notFound().build();
            } else if (e.getCause() instanceof BinlistTooManyException) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    private static String getIINFromCardNumber(CardRequest cardRequest) {
        return cardRequest.cardNumber().substring(0, 6);
    }
}
