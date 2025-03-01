package com.task.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> validationException(MethodArgumentNotValidException e) {
        HashMap<String, String> problems = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            problems.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(problems);
    }

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
}
