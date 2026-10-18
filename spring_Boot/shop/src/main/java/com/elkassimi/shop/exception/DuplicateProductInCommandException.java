package com.elkassimi.shop.exception;

public class DuplicateProductInCommandException extends RuntimeException {
    public DuplicateProductInCommandException(String message) {
        super(message);
    }
}
