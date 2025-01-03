package com.payments.payments_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NumberDetails(
        @JsonProperty("length") Integer length,
        @JsonProperty("luhn") Boolean luhn
) { }
