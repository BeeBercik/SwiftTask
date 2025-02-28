package com.task.Services;

import com.task.DTO.BasicCodeResponse;
import com.task.DTO.BranchResponse;
import com.task.DTO.CountryResponse;
import com.task.DTO.HeadquarterResponse;
import com.task.Model.SwiftCode;
import com.task.Repositories.SwiftCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    public Optional<? extends BasicCodeResponse> getCodeDetails(String code) {
        Optional<SwiftCode> swiftCodeBox = this.swiftCodeRepository.findById(code);
        if(swiftCodeBox.isEmpty()) return Optional.empty();

        SwiftCode sc = swiftCodeBox.get();
        String prefix = code.substring(0, 8);
        if(sc.getSwiftCode().endsWith("XXX")) {
            List<BasicCodeResponse> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                    filter(c -> !c.getSwiftCode().endsWith("XXX"))
                    .map(c -> new BasicCodeResponse(
                            c.getAddress(),
                            c.getBankName(),
                            c.getCountryISO2(),
                            true,
                            c.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return Optional.of(new HeadquarterResponse(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    true,
                    sc.getSwiftCode(),
                    sc.getCountryName(),
                    branches
            ));
        } else {
            return Optional.of(new BranchResponse(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    false,
                    sc.getSwiftCode(),
                    sc.getCountryName()
            ));
        }
    }

    public Optional<CountryResponse> getCodesByCountry(String isoCode) {
        List<SwiftCode> codesByCountry = this.swiftCodeRepository.findByCountryISO2(isoCode);

        if(codesByCountry.isEmpty()) return Optional.empty();

        List<BasicCodeResponse> swiftCodes = new LinkedList<>();
        for (SwiftCode singleCode : codesByCountry) {
            swiftCodes.add(new BasicCodeResponse(
                    singleCode.getAddress(),
                    singleCode.getBankName(),
                    singleCode.getCountryISO2(),
                    singleCode.getSwiftCode().endsWith("XXX"),
                    singleCode.getSwiftCode()
            ));
        }
        return Optional.of(new CountryResponse(isoCode, codesByCountry.getFirst().getCountryName(), swiftCodes));
    }
}
