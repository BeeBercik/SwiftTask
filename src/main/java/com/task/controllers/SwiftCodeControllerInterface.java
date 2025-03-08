package com.task.controllers;

import com.task.dto.SwiftCodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;


public interface SwiftCodeControllerInterface {
    ResponseEntity<?> getSwiftCodeDetails(@PathVariable("swift-code") String swiftCode);
    ResponseEntity<?> getAllSwiftCodesByCountry(@PathVariable(name = "countryISO2code") String isoCode);
    ResponseEntity<HashMap<String, String>> addNewSwiftCode(@RequestBody SwiftCodeRequest swiftCodeRequest);
    ResponseEntity<HashMap<String, String>> deleteSwiftCode(@PathVariable(name = "swift-code") String swiftCode);
}
