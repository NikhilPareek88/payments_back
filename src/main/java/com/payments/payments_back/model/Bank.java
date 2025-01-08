package com.payments.payments_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Bank(
        @JsonProperty("name") String name,
        @JsonProperty("url") String url,
        @JsonProperty("phone") String phone,
        @JsonProperty("city") String city
) { }