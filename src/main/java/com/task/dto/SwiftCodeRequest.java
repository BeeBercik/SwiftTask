package com.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class SwiftCodeRequest {

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Bank name cannot be empty")
    private String bankName;

    @NotBlank(message = "Country ISO2 code cannot be empty")
    @Size(min = 2, max = 2, message = "Country ISO2 code must be 2 letters")
    private String countryISO2;

    @NotBlank(message = "Country name cannot be empty")
    private String countryName;

    @NotNull(message = "You mast specify either code is headquarter or not")
    private Boolean isHeadquarter;

    @NotBlank(message = "You must provide SWIFT code")
    private String swiftCode;
}
