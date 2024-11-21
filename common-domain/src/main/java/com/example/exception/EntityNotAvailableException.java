package com.example.exception;

public class EntityNotAvailableException extends RuntimeException {
    public EntityNotAvailableException(String message) {
        super(message);
    }
}
