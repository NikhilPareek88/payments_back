package com.payments.payments_back.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

@Validated
public record CardRequest(
        @JsonProperty("card_number")
        @Size(min = 8, max = 19, message = "Card number must be between 8 and 19 characters.")
        @Pattern(regexp = "^[0-9]+$", message = "Card number must contain digits only.")
        @NotNull
        String cardNumber
) {}