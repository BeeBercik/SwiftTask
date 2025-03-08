package com.task.services;

import com.task.dto.CountrySwiftCodesResponse;
import com.task.dto.SwiftCodeRequest;
import com.task.dto.SwiftCodeResponse;

import java.util.Optional;

public interface SwiftCodeServiceInterface {
    Optional<SwiftCodeResponse> getCodeDetails(String swiftCode);
    Optional<CountrySwiftCodesResponse> getCodesByCountry(String isoCode);
    boolean addNewSwiftCode(SwiftCodeRequest codeRequest);
    boolean deleteSwiftCode(String swiftCode);
}
