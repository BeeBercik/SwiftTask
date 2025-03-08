package com.task.services.impl;

import com.task.dto.*;
import com.task.exceptions.IncorrectIso2CodeException;
import com.task.exceptions.IncorrectSwiftCodeException;
import com.task.exceptions.IncorrectSwiftCodeRequestException;
import com.task.model.SwiftCode;
import com.task.repositories.SwiftCodeRepository;
import com.task.services.SwiftCodeServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SwiftCodeService implements SwiftCodeServiceInterface {

    private final SwiftCodeRepository swiftCodeRepository;

    public Optional<SwiftCodeResponse> getCodeDetails(String swiftCode) {
        this.validateSwiftCode(swiftCode);

        return this.swiftCodeRepository.findById(swiftCode).map(code -> {
            String prefix = swiftCode.substring(0, 8);
            if(this.isCodeHeadquarter(code)) {
                List<SwiftCodeResponse> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                        filter(c -> !this.isCodeHeadquarter(c))
                        .map(c -> new SwiftCodeResponse(
                                c.getAddress(),
                                c.getBankName(),
                                c.getCountryISO2(),
                                false,
                                c.getSwiftCode()
                        ))
                        .collect(Collectors.toList());

                return new SwiftCodeResponse(
                        code.getAddress(),
                        code.getBankName(),
                        code.getCountryISO2(),
                        code.getCountryName(),
                        true,
                        code.getSwiftCode(),
                        branches
                );
            } else {
                return new SwiftCodeResponse(
                        code.getAddress(),
                        code.getBankName(),
                        code.getCountryISO2(),
                        code.getCountryName(),
                        false,
                        code.getSwiftCode()
                );
            }
        });
    }

    public Optional<CountrySwiftCodesResponse> getCodesByCountry(String isoCode) {
        if(isoCode.length() != 2) throw new IncorrectIso2CodeException("ISO code must be 2 letters");
        List<SwiftCode> codesByCountry = this.swiftCodeRepository.findByCountryISO2(isoCode);
        if(codesByCountry.isEmpty()) return Optional.empty();

        List<SwiftCodeResponse> swiftCodes = codesByCountry.stream()
                .map(swiftCode -> new SwiftCodeResponse(
                        swiftCode.getAddress(),
                        swiftCode.getBankName(),
                        swiftCode.getCountryISO2(),
                        this.isCodeHeadquarter(swiftCode),
                        swiftCode.getSwiftCode()
                )).collect(Collectors.toList());

        return Optional.of(new CountrySwiftCodesResponse(isoCode, codesByCountry.get(0).getCountryName(), swiftCodes));
    }

    public boolean addNewSwiftCode(SwiftCodeRequest codeRequest) {
        this.validateSwiftCodeRequest(codeRequest);
        if(this.swiftCodeRepository.existsBySwiftCode(codeRequest.getSwiftCode())) return false;

        this.swiftCodeRepository.save(new SwiftCode(
                codeRequest.getSwiftCode(),
                codeRequest.getAddress(),
                codeRequest.getBankName(),
                codeRequest.getCountryISO2(),
                codeRequest.getCountryName()
        ));
        return true;
    }

    public boolean deleteSwiftCode(String swiftCode) {
        this.validateSwiftCode(swiftCode);

        Optional<SwiftCode> codeOpt = this.swiftCodeRepository.findById(swiftCode);
        if(codeOpt.isEmpty()) return false;
        this.swiftCodeRepository.delete(codeOpt.get());

        return true;
    }

    private boolean isCodeHeadquarter(SwiftCode swiftCode) {
        return swiftCode.getSwiftCode().endsWith("XXX");
    }

    private void validateSwiftCode(String swiftCode, Boolean isHeadquarter) {
        if (swiftCode == null || swiftCode.trim().isEmpty())
            throw new IncorrectSwiftCodeException("SWIFT code cannot be empty");

        if (swiftCode.length() != 8 && swiftCode.length() != 11)
            throw new IncorrectSwiftCodeException("SWIFT code must be either 8 or 11 letters");

        if (swiftCode.length() == 11 && !swiftCode.endsWith("XXX"))
            throw new IncorrectSwiftCodeException("Headquarter SWIFT code must end with XXX");

        if (swiftCode.length() == 8 && swiftCode.endsWith("XXX"))
            throw new IncorrectSwiftCodeException("Branch SWIFT code cannot end with XXX");

        if (isHeadquarter != null && isHeadquarter && swiftCode.length() != 11)
            throw new IncorrectSwiftCodeException("Headquarter SWIFT code must be 11 letters and end with XXX");

        if (isHeadquarter != null && !isHeadquarter && swiftCode.length() != 8)
            throw new IncorrectSwiftCodeException("Branch SWIFT code must be 8 letters and cannot end with XXX");
    }

    private void validateSwiftCode(String swiftCode) {
        this.validateSwiftCode(swiftCode, null);
    }

    private void validateSwiftCodeRequest(SwiftCodeRequest codeRequest) {
        if (codeRequest.getAddress() == null || codeRequest.getAddress().trim().isEmpty())
            throw new IncorrectSwiftCodeRequestException("Address cannot be empty");

        if (codeRequest.getBankName() == null || codeRequest.getBankName().trim().isEmpty())
            throw new IncorrectSwiftCodeRequestException("Bank name cannot be empty");

        if (codeRequest.getCountryISO2() == null || codeRequest.getCountryISO2().length() != 2)
            throw new IncorrectSwiftCodeRequestException("Country ISO2 code must be 2 letters");

        if (codeRequest.getCountryName() == null || codeRequest.getCountryName().trim().isEmpty())
            throw new IncorrectSwiftCodeRequestException("Country name cannot be empty");

        if (codeRequest.getIsHeadquarter() == null)
            throw new IncorrectSwiftCodeRequestException("You must specify whether the code represents a headquarter or not");

        validateSwiftCode(codeRequest.getSwiftCode(), codeRequest.getIsHeadquarter());
    }
}

