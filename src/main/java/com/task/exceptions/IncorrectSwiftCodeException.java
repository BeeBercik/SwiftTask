package com.task.exceptions;

/**
 * Exception thrown when invalid swift code is provided
 */
public class IncorrectSwiftCodeException extends RuntimeException {
    public IncorrectSwiftCodeException(String message) {
        super(message);
    }
}
