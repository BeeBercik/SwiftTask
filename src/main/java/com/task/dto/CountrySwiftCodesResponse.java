package com.task.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Represents a collection of swift codes for a specific country
 */
@Getter
@ToString
public class CountrySwiftCodesResponse {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeResponse> swiftCodes;

    public CountrySwiftCodesResponse(String countryISO2, String countryName, List<SwiftCodeResponse> swiftCodes) {
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.swiftCodes = swiftCodes;
    }
}
