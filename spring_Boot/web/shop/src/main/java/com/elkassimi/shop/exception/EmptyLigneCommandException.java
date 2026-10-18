package com.elkassimi.shop.exception;

public class EmptyLigneCommandException extends RuntimeException {
    public EmptyLigneCommandException(String message) {
        super(message);
    }
}
