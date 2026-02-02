package com.liaw.dev.BurgerQueen.exception.exceptions;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException(String message) {
        super(message);
    }
}
