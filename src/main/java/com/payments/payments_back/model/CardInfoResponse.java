package com.payments.payments_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields during serialization
public record CardInfoResponse(
        @JsonProperty("number") NumberDetails number,
        @JsonProperty("scheme") String scheme,
        @JsonProperty("type") String type,
        @JsonProperty("brand") String brand,
        @JsonProperty("prepaid") Boolean prepaid,
        @JsonProperty("country") Country country,
        @JsonProperty("bank") Bank bank
) { }