package com.payments.payments_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Country(
        @JsonProperty("numeric") String numeric,
        @JsonProperty("alpha2") String alpha2,
        @JsonProperty("name") String name,
        @JsonProperty("emoji") String emoji,
        @JsonProperty("currency") String currency,
        @JsonProperty("latitude") Integer latitude,
        @JsonProperty("longitude") Integer longitude
) { }