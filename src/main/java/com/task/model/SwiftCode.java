package com.task.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "codes")
public class SwiftCode {
    @Id
    @Column(name = "swift_code", nullable = false)
    private String swiftCode;

    private String address;

    @Column(name = "name", nullable = false)
    private String bankName;

    @Column(name = "iso2_code", nullable = false)
    private String countryISO2;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "town_name")
    private String townName;

    @Column(name = "time_zone")
    private String timeZone;

    public SwiftCode(String swiftCode, String address, String bankName, String countryISO2, String countryName) {
        this.swiftCode = swiftCode;
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
    }
}
