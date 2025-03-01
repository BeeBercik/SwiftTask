package com.task.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CountryResponse {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeResponse> swiftCodes;

    public CountryResponse(String countryISO2, String countryName, List<SwiftCodeResponse> swiftCodes) {
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.swiftCodes = swiftCodes;
    }
}
