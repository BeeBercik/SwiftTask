package com.task.model;

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
}
