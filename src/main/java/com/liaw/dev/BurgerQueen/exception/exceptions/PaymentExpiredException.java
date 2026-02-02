package com.liaw.dev.BurgerQueen.exception.exceptions;

public class PaymentExpiredException extends RuntimeException {
    public PaymentExpiredException(String message) {
        super(message);
    }
}
