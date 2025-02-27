package com.task.Services;

import com.task.DTO.BasicCode;
import com.task.DTO.Branch;
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

    public Optional<? extends BasicCode> getCodeDetails(String code) {
        Optional<SwiftCode> swiftCodeBox = this.swiftCodeRepository.findById(code);
        if(swiftCodeBox.isEmpty()) return Optional.empty();

        SwiftCode sc = swiftCodeBox.get();
        String prefix = code.substring(0, 8);
        if(sc.getSwiftCode().endsWith("XXX")) {
            List<BasicCode> branches = this.swiftCodeRepository.findBySwiftCodeStartingWith(prefix).stream().
                    filter(c -> !c.getSwiftCode().endsWith("XXX"))
                    .map(c -> new BasicCode(
                            c.getAddress(),
                            c.getBankName(),
                            c.getCountryISO2(),
                            true,
                            c.getSwiftCode()
                    ))
                    .collect(Collectors.toList());

            return Optional.of(new Headquarter(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    true,
                    sc.getSwiftCode(),
                    sc.getCountryName(),
                    branches
            ));
        } else {
            return Optional.of(new Branch(
                    sc.getAddress(),
                    sc.getBankName(),
                    sc.getCountryISO2(),
                    false,
                    sc.getSwiftCode(),
                    sc.getCountryName()
            ));
        }
    }
}
