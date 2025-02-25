package com.task.Services;

import com.task.DTO.Branch;
import com.task.DTO.CodeResponse;
import com.task.DTO.Headquarter;
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

    public Optional<CodeResponse> getCodeDetails(String code) {
        Optional<SwiftCode> swiftCodeBox = this.swiftCodeRepository.findById(code);
        if(swiftCodeBox.isEmpty()) return Optional.empty();

        SwiftCode sc = swiftCodeBox.get();
        String prefix = code.substring(0, 8);
        if(sc.getSwiftCode().endsWith("XXX")) {
            List<Branch> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                    filter(c -> !c.getSwiftCode().endsWith("XXX"))
                    .map(c -> new Branch(
                            c.getAddress(),
                            c.getBankName(),
                            c.getCountryISO2(),
                            c.getCountryName(),
                            true,
                            c.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return Optional.of(new Headquarter(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    sc.getCountryName(),
                    true,
                    sc.getSwiftCode(),
                    branches
            ));
        } else {
            return Optional.of(new Branch(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    sc.getCountryName(),
                    false,
                    sc.getSwiftCode()
            ));
        }
    }
}
