package com.sree.inventory.exception;

public class NotSufficientQuantityAvailableException extends RuntimeException{
    public NotSufficientQuantityAvailableException(String message) {
        super(message);
    }
}
