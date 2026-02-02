package com.liaw.dev.BurgerQueen.exception.exceptions;

public class CategoryExistException extends RuntimeException {
    public CategoryExistException(String message) {
        super(message);
    }
}
