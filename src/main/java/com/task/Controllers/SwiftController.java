package com.task.Controllers;

import com.task.DTO.BasicCodeResponse;
import com.task.DTO.CountryResponse;
import com.task.Services.SwiftCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SwiftController {

    private final SwiftCodeService swiftCodeService;

    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<? extends BasicCodeResponse> getCodeDetails(@PathVariable("swift-code") String code) {
        Optional<? extends BasicCodeResponse> codeBox = this.swiftCodeService.getCodeDetails(code);

        return codeBox.isPresent() ? ResponseEntity.ok(codeBox.get()) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/v1/swift-codes/country/{countryISO2code}")
    public ResponseEntity<?> getCodesByCountry(@PathVariable(name = "countryISO2code") String isoCode) {
        Optional<CountryResponse> codeBox = this.swiftCodeService.getCodesByCountry(isoCode);
        System.out.println(codeBox);
        return codeBox.isPresent() ? ResponseEntity.ok(codeBox.get()) : ResponseEntity.badRequest().build();
    }
}
