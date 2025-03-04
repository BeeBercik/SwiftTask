package com.task.services;

import com.task.dto.*;
import com.task.exceptions.IncorrectIso2Code;
import com.task.exceptions.IncorrectSwiftCode;
import com.task.model.SwiftCode;
import com.task.repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    public Optional<SwiftCodeResponse> getCodeDetails(String swiftCode) {
        this.checkSwiftCodeLength(swiftCode);

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

    public Optional<CountryResponse> getCodesByCountry(String isoCode) {
        if(isoCode.length() != 2) throw new IncorrectIso2Code("ISO code must be 2 letters");
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

        return Optional.of(new CountryResponse(isoCode, codesByCountry.getFirst().getCountryName(), swiftCodes));
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
        this.checkSwiftCodeLength(swiftCode);

        Optional<SwiftCode> codeOpt = this.swiftCodeRepository.findById(swiftCode);
        if(codeOpt.isEmpty()) return false;
        this.swiftCodeRepository.delete(codeOpt.get());

        return true;
    }

    private boolean isCodeHeadquarter(SwiftCode swiftCode) {
        return swiftCode.getSwiftCode().endsWith("XXX");
    }

    private void checkSwiftCodeLength(String swiftCode) {
        if(swiftCode.length() != 8 && swiftCode.length() != 11)
            throw new IncorrectSwiftCode("SWIFT code must be 8 or 11 letters");
    }

    private void validateSwiftCodeRequest(SwiftCodeRequest codeRequest) {
        if(codeRequest.getIsHeadquarter() && !codeRequest.getSwiftCode().endsWith("XXX"))
            throw new IncorrectSwiftCode("Headquarter must ends with XXX");
        if(!codeRequest.getIsHeadquarter() && codeRequest.getSwiftCode().endsWith("XXX"))
            throw new IncorrectSwiftCode("Branch cannot ends with XXX");
        if(codeRequest.getIsHeadquarter() && codeRequest.getSwiftCode().length() != 11)
            throw new IncorrectSwiftCode("Headquarter bust me 11 letters");
        if(!codeRequest.getIsHeadquarter() && codeRequest.getSwiftCode().length() != 8)
            throw new IncorrectSwiftCode("Branch bust me 8 letters");
    }
}

