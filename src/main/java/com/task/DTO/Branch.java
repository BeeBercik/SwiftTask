package com.task.DTO;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Branch extends BasicCode {
    private String countryName;

    public Branch(String address, String bankName, String countryISO2, boolean isHeadquarter, String swiftCode, String countryName) {
        super(address, bankName, countryISO2, isHeadquarter, swiftCode);
        this.countryName = countryName;
    }
}
