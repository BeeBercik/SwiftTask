package com.task.exceptions;

public class IncorrectSwiftCodeRequestException extends RuntimeException {
    public IncorrectSwiftCodeRequestException(String message) {
        super(message);
    }
}
