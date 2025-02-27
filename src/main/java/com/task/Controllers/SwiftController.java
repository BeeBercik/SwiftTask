package com.task.Controllers;

import com.task.DTO.BasicCode;
import com.task.DTO.CountryResponse;
import com.task.Services.SwiftCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SwiftController {

    private final SwiftCodeService swiftCodeService;

    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<? extends BasicCode> getCodeDetails(@PathVariable("swift-code") String code) {
        Optional<? extends BasicCode> codeBox = this.swiftCodeService.getCodeDetails(code);
        System.out.println(codeBox);

        return codeBox.isPresent() ? ResponseEntity.ok(codeBox.get()) : ResponseEntity.badRequest().build();
    }
}
