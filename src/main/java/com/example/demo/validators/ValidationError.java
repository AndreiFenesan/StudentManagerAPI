package com.example.demo.validators;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}
