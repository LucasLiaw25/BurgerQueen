package com.liaw.dev.BurgerQueen.exception.exceptions;

public class ProductExistException extends RuntimeException {
    public ProductExistException(String message) {
        super(message);
    }
}
