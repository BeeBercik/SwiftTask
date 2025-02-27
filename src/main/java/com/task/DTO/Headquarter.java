package com.task.DTO;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(callSuper = true)
public class Headquarter extends BasicCode {
    private String countryName;
    private List<BasicCode> branches;

    public Headquarter(String address, String bankName, String countryISO2, boolean isHeadquarter, String swiftCode, String countryName, List<BasicCode> branches) {
        super(address, bankName, countryISO2, isHeadquarter, swiftCode);
        this.countryName = countryName;
        this.branches = branches;
    }
}
