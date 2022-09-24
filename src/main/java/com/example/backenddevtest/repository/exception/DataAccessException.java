package com.example.backenddevtest.repository.exception;

/**
 * To simplify error handling, the repositories tries to unify all underlying exceptions
 * as {@link DataAccessException}. In this way, the error handling between layers is simplified
 * and implementation details are not exposed easily.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable throwable) {
        super(throwable);
    }
}
