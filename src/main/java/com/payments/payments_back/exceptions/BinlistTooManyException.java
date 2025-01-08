package com.payments.payments_back.exceptions;

public class BinlistTooManyException extends RuntimeException {
    public BinlistTooManyException(String message) {
        super(message);
    }
}
