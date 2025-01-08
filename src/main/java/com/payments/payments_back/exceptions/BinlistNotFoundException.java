package com.payments.payments_back.exceptions;

public class BinlistNotFoundException extends RuntimeException {
    public BinlistNotFoundException(String message) {
        super(message);
    }
}
