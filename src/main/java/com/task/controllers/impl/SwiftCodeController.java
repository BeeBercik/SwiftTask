package com.task.controllers.impl;

import com.task.controllers.SwiftCodeControllerInterface;
import com.task.dto.SwiftCodeResponse;
import com.task.dto.CountrySwiftCodesResponse;
import com.task.dto.SwiftCodeRequest;
import com.task.services.impl.SwiftCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

/**
 * REST Controller for managing swift codes.
 * Provides endpoints for retrieving, adding, and deleting swift codes
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController implements SwiftCodeControllerInterface {

    private final SwiftCodeService swiftCodeService;

    /**
     * Method used to retrieve specific swift code
     * @param swiftCode given swift code
     * @return ResponseEntity with appropriate status code and code details if exists, otherwise just not-found code
     */
    @GetMapping("/{swift-code}")
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable("swift-code") String swiftCode) {
        Optional<SwiftCodeResponse> codeBox = this.swiftCodeService.getCodeDetails(swiftCode);
        return codeBox.isPresent()
                ? ResponseEntity.ok(codeBox.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.createResponseMessage("Swift code not exists"));
    }

    /**
     * Method used to get all swift codes associated with specific country
     * @param isoCode given iso2 code
     * @return ResponseEntity with swift codes list of given iso2 code otherwise not-found status
     */
    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> getAllSwiftCodesByCountry(@PathVariable(name = "countryISO2code") String isoCode) {
        Optional<CountrySwiftCodesResponse> codeBox = this.swiftCodeService.getCodesByCountry(isoCode.toUpperCase());
        return codeBox.isPresent()
                ? ResponseEntity.ok(codeBox.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.createResponseMessage("No Swift codes with such country ISO code"));
    }

    /**
     * Adds new swift code to the database
     * @param swiftCodeRequest request with code swift details
     * @return ResponseEntity with right message and status code - ok status if added otherwise bad-request
     */
    @PostMapping()
    public ResponseEntity<HashMap<String, String>> addNewSwiftCode(@RequestBody SwiftCodeRequest swiftCodeRequest) {
        boolean result = this.swiftCodeService.addNewSwiftCode(swiftCodeRequest);
        return result
                ? ResponseEntity.ok(this.createResponseMessage("Swift code added"))
                : ResponseEntity.badRequest().body(this.createResponseMessage("Swift code already exists"));
    }

    /**
     * Deletes an existing swift code from the system
     *
     * @param swiftCode The swift code to delete.
     * @return ResponseEntity with a success message if the SWIFT code was deleted or bad-request response if it doesnt exist
     */
    @DeleteMapping("/{swift-code}")
    public ResponseEntity<HashMap<String, String>> deleteSwiftCode(@PathVariable(name = "swift-code") String swiftCode) {
        boolean result = this.swiftCodeService.deleteSwiftCode(swiftCode);
        return result
                ? ResponseEntity.ok(this.createResponseMessage("Swift code deleted"))
                : ResponseEntity.badRequest().body(this.createResponseMessage("Swift code not exists"));
    }

    /**
     * Creates a response message to be returned in the API response
     *
     * @param message message to be included in the response
     * @return HashMap containing the message
     */
    private HashMap<String, String> createResponseMessage(String message) {
        HashMap<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", message);
        return responseMessage;
    }
}
