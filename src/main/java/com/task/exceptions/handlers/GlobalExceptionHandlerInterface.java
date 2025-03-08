package com.task.exceptions.handlers;

import com.task.exceptions.IncorrectIso2CodeException;
import com.task.exceptions.IncorrectSwiftCodeException;
import com.task.exceptions.IncorrectSwiftCodeRequestException;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface GlobalExceptionHandlerInterface {
    ResponseEntity<HashMap<String, String>> incorrectIso2Code(IncorrectIso2CodeException e);
    ResponseEntity<HashMap<String, String>> incorrectSwiftCode(IncorrectSwiftCodeException e);
    ResponseEntity<HashMap<String, String>> IncorrectSwiftCodeRequest(IncorrectSwiftCodeRequestException e);
}
