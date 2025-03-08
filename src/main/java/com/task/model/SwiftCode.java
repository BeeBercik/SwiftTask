package com.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "codes")
public class SwiftCode {
    @Id
    @Column(name = "swift code", nullable = false, length = 11)
    private String swiftCode;

    @Column(name = "address")
    private String address;

    @Column(name = "name", nullable = false)
    private String bankName;

    @Column(name = "country iso2 code", nullable = false, length = 2)
    private String countryISO2;

    @Column(name = "country name", nullable = false)
    private String countryName;

    @Column(name = "code type")
    private String codeType;

    @Column(name = "town name")
    private String townName;

    @Column(name = "time zone")
    private String timeZone;

    public SwiftCode(String swiftCode, String address, String bankName, String countryISO2, String countryName) {
        this.swiftCode = swiftCode;
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2.toUpperCase();
        this.countryName = countryName.toUpperCase();
    }
}
