package com.task.exceptions.handlers;

import com.task.exceptions.IncorrectIso2CodeException;
import com.task.exceptions.IncorrectSwiftCodeException;
import com.task.exceptions.IncorrectSwiftCodeRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Global exception handler for the application.
 * This class catches specific exceptions related to incorrect SWIFT codes and incorrect ISO country codes, returning appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler implements GlobalExceptionHandlerInterface {

    /**
     * Handles custom {@link IncorrectIso2CodeException} which is thrown when iso country code is incorrect
     * @param e thrown exception object with details
     * @return  object with appropriate status code and message
     */
    @ExceptionHandler(IncorrectIso2CodeException.class)
    public ResponseEntity<HashMap<String, String>> incorrectIso2Code(IncorrectIso2CodeException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * Handles custom {@link IncorrectSwiftCodeException} which is thrown when swift code is incorrect
     * @param e thrown exception object with details
     * @return  object with appropriate status code and message
     */
    @ExceptionHandler(IncorrectSwiftCodeException.class)
    public ResponseEntity<HashMap<String, String>> incorrectSwiftCode(IncorrectSwiftCodeException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }

    /**
     * Handles custom {@link IncorrectSwiftCodeRequestException} which is thrown when swift code request is incorrect
     * @param e thrown exception object with details
     * @return  object with appropriate status code and message
     */
    @ExceptionHandler(IncorrectSwiftCodeRequestException.class)
    public ResponseEntity<HashMap<String, String>> IncorrectSwiftCodeRequest(IncorrectSwiftCodeRequestException e) {
        HashMap<String, String> problem = new HashMap<>();
        problem.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(problem);
    }
}
