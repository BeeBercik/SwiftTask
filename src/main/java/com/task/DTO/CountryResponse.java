package com.task.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class CountryResponse {
    private String countryISO2;
    private String countryName;
    private List<BasicCode> swiftCodes;
}
