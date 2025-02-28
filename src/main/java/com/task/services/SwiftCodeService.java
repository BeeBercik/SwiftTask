package com.task.Services;

import com.task.DTO.*;
import com.task.Model.SwiftCode;
import com.task.Repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    public Optional<BasicCodeResponse> getCodeDetails(String swiftCode) {
        Optional<SwiftCode> swiftCodeOpt = this.swiftCodeRepository.findById(swiftCode);
        if(swiftCodeOpt.isEmpty()) return Optional.empty();

        SwiftCode code = swiftCodeOpt.get();
        String prefix = swiftCode.substring(0, 8);
        if(this.isCodeHeadquarter(code)) {
            List<BasicCodeResponse> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                    filter(c -> !this.isCodeHeadquarter(c))
                    .map(c -> new BasicCodeResponse(
                            c.getAddress(),
                            c.getBankName(),
                            c.getCountryISO2(),
                            true,
                            c.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return Optional.of(new HeadquarterResponse(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountryISO2(),
                    true,
                    code.getSwiftCode(),
                    code.getCountryName(),
                    branches
            ));
        } else {
            return Optional.of(new BranchResponse(
                    code.getAddress(),
                    code.getBankName(),
                    code.getCountryISO2(),
                    false,
                    code.getSwiftCode(),
                    code.getCountryName()
            ));
        }
    }

    public Optional<CountryResponse> getCodesByCountry(String isoCode) {
        List<SwiftCode> codesByCountry = this.swiftCodeRepository.findByCountryISO2(isoCode);
        if(codesByCountry.isEmpty()) return Optional.empty();

        List<BasicCodeResponse> swiftCodes = codesByCountry.stream()
                .map(swiftCode -> new BasicCodeResponse(
                        swiftCode.getAddress(),
                        swiftCode.getBankName(),
                        swiftCode.getCountryISO2(),
                        this.isCodeHeadquarter(swiftCode),
                        swiftCode.getSwiftCode()
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

