package com.payments.payments_back.model;

import java.math.BigDecimal;

public record CardCostResponse(String country, BigDecimal cost) { }
