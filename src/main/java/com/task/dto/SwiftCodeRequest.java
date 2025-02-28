package com.task.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class SwiftCodeRequest extends BasicCodeResponse {
    private String countryName;

    public SwiftCodeRequest(String address, String bankName, String countryISO2, boolean isHeadquarter, String swiftCode, String countryName) {
        super(address, bankName, countryISO2, isHeadquarter, swiftCode);
        this.countryName = countryName;
    }
}
