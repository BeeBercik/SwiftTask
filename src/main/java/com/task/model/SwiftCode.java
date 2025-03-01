package com.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Table(name = "codes")
public class SwiftCode {
    @Id
    @Column(name = "swift_code", nullable = false, length = 11)
    private String swiftCode;

    private String address;

    @Column(name = "name", nullable = false)
    private String bankName;

    @Column(name = "iso2_code", nullable = false, length = 2)
    private String countryISO2;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    public SwiftCode(String swiftCode, String address, String bankName, String countryISO2, String countryName) {
        this.swiftCode = swiftCode;
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
    }
}
