package com.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Represents a response containing details about a SWIFT code.
 * This class is used for serializing and deserializing JSON responses.
 */
@NoArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "countryName",
        "isHeadquarter",
        "swiftCode",
        "branches"
})
public class SwiftCodeResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;

    private String swiftCode;
    private List<SwiftCodeResponse> branches;

    /**
     * Constructor for a headquarter swift code with associated branches.
     *
     * @param address       address of the headquarter.
     * @param bankName      name of the bank.
     * @param countryISO2   iso 2-letter country code.
     * @param countryName   full name of the country.
     * @param isHeadquarter whether this is a headquarter SWIFT code.
     * @param swiftCode     SWIFT code.
     * @param branches      list of associated branch SWIFT codes.
     */
    public SwiftCodeResponse(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode, List<SwiftCodeResponse> branches) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
        this.branches = branches;
    }

    /**
     * Constructor for a branch SWIFT code inside a headquarter's branch array.
     *
     * @param address       address of the branch.
     * @param bankName      name of the bank.
     * @param countryISO2   ISO 2-letter country code.
     * @param isHeadquarter whether this SWIFT code belongs to a headquarter.
     * @param swiftCode     SWIFT code.
     */
    public SwiftCodeResponse(String address, String bankName, String countryISO2,  boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    /**
     * Constructor for an independent branch swift code.
     * Used in {@link CountrySwiftCodesResponse} to represent a swift code that is part of a country's SWIFT code list.
     *
     * @param address       address of the branch.
     * @param bankName      name of the bank.
     * @param countryISO2   iso 2-letter country code.
     * @param countryName   full name of the country.
     * @param isHeadquarter whether this SWIFT code belongs to a headquarter.
     * @param swiftCode     SWIFT code.
     */
    public SwiftCodeResponse(String address, String bankName, String countryISO2,  String countryName, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }
}
