package com.task.services;

import com.task.dto.*;
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
        Optional<SwiftCode> swiftCodeOpt = this.swiftCodeRepository.findById(swiftCode);
        if(swiftCodeOpt.isEmpty()) return Optional.empty();

        SwiftCode code = swiftCodeOpt.get();
        String prefix = swiftCode.substring(0, 8);
        if(this.isCodeHeadquarter(code)) {
            List<SwiftCodeResponse> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                    filter(c -> !this.isCodeHeadquarter(c))
                    .map(c -> new SwiftCodeResponse(
                            c.getAddress(),
                            c.getBankName(),
                            c.getCountryISO2(),
                            c.getSwiftCode(),
                            true
                    ))
                    .collect(Collectors.toList());

            return Optional.of(new SwiftCodeResponse(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountryISO2(),
                    code.getCountryName(),
                    code.getSwiftCode(),
                    true,
                    branches
            ));
        } else {
            return Optional.of(new SwiftCodeResponse(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountryISO2(),
                    code.getSwiftCode(),
                    code.getCountryName(),
                    false
            ));
        }
    }

    public Optional<CountryResponse> getCodesByCountry(String isoCode) {
        List<SwiftCode> codesByCountry = this.swiftCodeRepository.findByCountryISO2(isoCode);
        if(codesByCountry.isEmpty()) return Optional.empty();

        List<SwiftCodeResponse> swiftCodes = codesByCountry.stream()
                .map(swiftCode -> new SwiftCodeResponse(
                        swiftCode.getAddress(),
                        swiftCode.getBankName(),
                        swiftCode.getCountryISO2(),
                        swiftCode.getSwiftCode(),
                        this.isCodeHeadquarter(swiftCode)
                )).collect(Collectors.toList());
        return Optional.of(new CountryResponse(isoCode, codesByCountry.getFirst().getCountryName(), swiftCodes));
    }

    public boolean addNewSwiftCode(SwiftCodeRequest codeRequest) {
        if(this.swiftCodeRepository.existsBySwiftCode(codeRequest.getSwiftCode())) return false;
        this.swiftCodeRepository.save(new SwiftCode(
                codeRequest.isHeadquarter() ? codeRequest.getSwiftCode() + "XXX" : codeRequest.getSwiftCode(),
                codeRequest.getAddress(),
                codeRequest.getBankName(),
                codeRequest.getCountryISO2(),
                codeRequest.getCountryName()
        ));
        return true;
    }

    public boolean deleteSwiftCode(String swiftCode) {
        if(!this.swiftCodeRepository.existsBySwiftCode(swiftCode)) return false;
        this.swiftCodeRepository.delete(this.swiftCodeRepository.findSwiftCodeBySwiftCode(swiftCode));
        return true;
    }

    private boolean isCodeHeadquarter(SwiftCode swiftCode) {
        return swiftCode.getSwiftCode().endsWith("XXX");
    }
}

