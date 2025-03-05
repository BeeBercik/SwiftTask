package com.task.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectIso2Code.class)
    public ResponseEntity<HashMap<String, String>> incorrectIso2Code(IncorrectIso2Code e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(IncorrectSwiftCode.class)
    public ResponseEntity<HashMap<String, String>> incorrectSwiftCode(IncorrectSwiftCode e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(IncorrectSwiftCodeRequest.class)
    public ResponseEntity<HashMap<String, String>> IncorrectSwiftCodeRequest(IncorrectSwiftCodeRequest e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }
}
