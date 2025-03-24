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

/**
 * Service class for managing SWIFT codes.
 * This class provides methods for retrieving, adding, and deleting swift codes as well as validating their correctness
 */
@Service
@RequiredArgsConstructor
public class SwiftCodeService implements SwiftCodeServiceInterface {

    private final SwiftCodeRepository swiftCodeRepository;

    /**
     * Retrieves details of a specific swift code
     * @param swiftCode swift code to search for
     * @return Optional of {@link SwiftCodeResponse} if found, otherwise empty
     * @throws IncorrectSwiftCodeException if swift code is incorrect
     */
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

    /**
     * Retrieves all swift codes associated with given iso2 country code
     * @param isoCode given iso2 code
     * @return optional of {@link CountrySwiftCodesResponse} if swift codes exist for the country otherwise empty
     * @throws IncorrectIso2CodeException if iso2 code is invalid
     */
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

    /**
     * Adds new swift code to the database
     * @param codeRequest request containing swift code details to add
     * @return true if code was added successfully otherwise false
     * @throws IncorrectSwiftCodeRequestException if provided request is invalid
     */
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

    /**
     * Deletes existing swift code from the database
     * @param swiftCode given swift code to delete
     * @return true if code was deleted successfully otherwise false
     * @throws IncorrectSwiftCodeException if given swift code is invalid
     */
    public boolean deleteSwiftCode(String swiftCode) {
        this.validateSwiftCode(swiftCode);

        Optional<SwiftCode> codeOpt = this.swiftCodeRepository.findById(swiftCode);
        if(codeOpt.isEmpty()) return false;
        this.swiftCodeRepository.delete(codeOpt.get());

        return true;
    }

    /**
     * Checks whether given swift code is headquarter or branch
     * @param swiftCode given swift code to check
     * @return true if swift code is headquarter otherwise false
     */
    private boolean isCodeHeadquarter(SwiftCode swiftCode) {
        return swiftCode.getSwiftCode().endsWith("XXX");
    }

    /**
     * Validates swift code - its format and correctness
     * @param swiftCode given swift code to validate
     * @param isHeadquarter says if given swift code is headquarter or not
     * @throws IncorrectSwiftCodeException if swift code is invalid
     */
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

    /**
     * Validates the format of a swift code without checking if it is a headquarter
     * @param swiftCode given swift code to validate
     * @throws IncorrectSwiftCodeException if the SWIFT code is invalid
     */
    private void validateSwiftCode(String swiftCode) {
        this.validateSwiftCode(swiftCode, null);
    }

    /**
     * Validates swift code request to add new swift code
     * @param codeRequest given request with swift code details to add
     * @throws IncorrectSwiftCodeRequestException when code request is incorrect
     */
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

