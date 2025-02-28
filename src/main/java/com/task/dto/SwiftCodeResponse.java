package com.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor // headquarter constructor
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwiftCodeResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private String swiftCode;
    private boolean isHeadquarter;
    private List<SwiftCodeResponse> branches;

    // branch in headquarter constructor
    public SwiftCodeResponse(String address, String bankName, String countryISO2, String swiftCode, boolean isHeadquarter) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.swiftCode = swiftCode;
        this.isHeadquarter = isHeadquarter;
    }

    // branch constructor, object in country list
    public SwiftCodeResponse(String address, String bankName, String countryISO2,  String countryName, String swiftCode, boolean isHeadquarter) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCode = swiftCode;
        this.isHeadquarter = isHeadquarter;
    }
}
