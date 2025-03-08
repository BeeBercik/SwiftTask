package com.task.exceptions.handlers;

import com.task.exceptions.IncorrectIso2CodeException;
import com.task.exceptions.IncorrectSwiftCodeException;
import com.task.exceptions.IncorrectSwiftCodeRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler implements GlobalExceptionHandlerInterface {

    @ExceptionHandler(IncorrectIso2CodeException.class)
    public ResponseEntity<HashMap<String, String>> incorrectIso2Code(IncorrectIso2CodeException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(IncorrectSwiftCodeException.class)
    public ResponseEntity<HashMap<String, String>> incorrectSwiftCode(IncorrectSwiftCodeException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(IncorrectSwiftCodeRequestException.class)
    public ResponseEntity<HashMap<String, String>> IncorrectSwiftCodeRequest(IncorrectSwiftCodeRequestException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }
}
