package com.task.exceptions;

/**
 * Exception thrown when swift code request contains invalid or missing data
 */
public class IncorrectSwiftCodeRequestException extends RuntimeException {
    public IncorrectSwiftCodeRequestException(String message) {
        super(message);
    }
}
