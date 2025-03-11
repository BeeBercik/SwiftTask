package com.task.exceptions;

/**
 * Exception thrown when invalid iso2 code is provided
 */
public class IncorrectIso2CodeException extends RuntimeException {
    public IncorrectIso2CodeException(String message) {
        super(message);
    }
}
