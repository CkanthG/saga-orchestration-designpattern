package com.sree.payment.exception;

public class InSufficientAmountException extends RuntimeException{
    public InSufficientAmountException(String message) {
        super(message);
    }
}
