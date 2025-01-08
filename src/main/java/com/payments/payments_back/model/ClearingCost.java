package com.payments.payments_back.model;

import java.math.BigDecimal;

public record ClearingCost(String country, BigDecimal cost) {
}
