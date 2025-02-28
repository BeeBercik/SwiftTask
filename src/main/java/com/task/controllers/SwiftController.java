package com.task.Controllers;

import com.task.DTO.BasicCodeResponse;
import com.task.DTO.CountryResponse;
import com.task.DTO.SwiftCodeRequest;
import com.task.Services.SwiftCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/swift-codes")
public class SwiftController {

    private final SwiftCodeService swiftCodeService;

    @GetMapping("/{swift-code}")
    public ResponseEntity<?> getSwiftCodeDetails(@PathVariable("swift-code") String code) {
        Optional<BasicCodeResponse> codeBox = this.swiftCodeService.getCodeDetails(code);
        return codeBox.isPresent()
                ? ResponseEntity.ok(codeBox.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.createResponseMessage("No Swift code with " + code + " code"));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<?> getAllSwiftCodesByCountry(@PathVariable(name = "countryISO2code") String isoCode) {
        Optional<CountryResponse> codeBox = this.swiftCodeService.getCodesByCountry(isoCode);
        return codeBox.isPresent()
                ? ResponseEntity.ok(codeBox.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.createResponseMessage("No Swift codes with " + isoCode + " country ISO code"));
    }

    @PostMapping()
    public ResponseEntity<HashMap<String, String>> addNewSwiftCode(@RequestBody SwiftCodeRequest swiftCodeRequest) {
        boolean result = this.swiftCodeService.addNewSwiftCode(swiftCodeRequest);
        return result
                ? ResponseEntity.ok(this.createResponseMessage("Swift code added"))
                : ResponseEntity.badRequest().body(this.createResponseMessage("Swift code already exists"));
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<HashMap<String, String>> deleteSwiftCode(@PathVariable(name = "swift-code") String swiftCode) {
        boolean result = this.swiftCodeService.deleteSwiftCode(swiftCode);
        return result
                ? ResponseEntity.ok(this.createResponseMessage("deleted"))
                : ResponseEntity.badRequest().body(this.createResponseMessage("not deleted"));
    }

    private HashMap<String, String> createResponseMessage(String message) {
        HashMap<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", message);
        return responseMessage;
    }
}
