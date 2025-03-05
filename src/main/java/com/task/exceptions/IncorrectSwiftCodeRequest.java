package com.task.exceptions;

public class IncorrectSwiftCodeRequest extends RuntimeException {
    public IncorrectSwiftCodeRequest(String message) {
        super(message);
    }
}
