package com.task.Controllers;

import com.task.DTO.CodeResponse;
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
    public ResponseEntity<CodeResponse> getCodeDetails(@PathVariable("swift-code") String code) {
        Optional<CodeResponse> codeBox = this.swiftCodeService.getCodeDetails(code);
        System.out.println(codeBox);

        return codeBox.isPresent() ? ResponseEntity.ok(codeBox.get()) : ResponseEntity.badRequest().build();
    }
}
