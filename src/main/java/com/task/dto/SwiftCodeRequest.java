package com.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a request to create new swift code
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SwiftCodeRequest {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private Boolean isHeadquarter;
    private String swiftCode;
}
