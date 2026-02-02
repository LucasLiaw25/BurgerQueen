package com.liaw.dev.BurgerQueen.exception.exceptions;

public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
