package com.example.backenddevtest.repository.exception;

/**
 * Exception that represent a situation where
 * the required data has been not found or not exists.
 */
public class DataNotFoundException extends DataAccessException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
