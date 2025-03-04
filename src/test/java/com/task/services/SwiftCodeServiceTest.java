package com.task.services;

import com.task.dto.CountryResponse;
import com.task.dto.SwiftCodeResponse;
import com.task.exceptions.IncorrectIso2Code;
import com.task.exceptions.IncorrectSwiftCode;
import com.task.model.SwiftCode;
import com.task.repositories.SwiftCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SwiftCodeServiceTest {

    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @InjectMocks
    private SwiftCodeService swiftCodeService;

    @BeforeEach
    public void set() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckSwiftCodeLength() {
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("1234567"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("123456789"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("123456789012"));
    }

    @Test
    public void testCheckIfSwiftCodeExists() {
        when(this.swiftCodeRepository.findById("12345678")).thenReturn(Optional.empty());

        assertTrue(this.swiftCodeService.getCodeDetails("12345678").isEmpty());
    }

    @Test
    public void testHeadquarter() {
        Optional<SwiftCodeResponse> headquarterOpt = this.getHeadquarter();

        assertTrue(headquarterOpt.isPresent());
        SwiftCodeResponse headquarter = headquarterOpt.get();
        assertTrue(headquarter.isHeadquarter());
        assertEquals(1, headquarter.getBranches().size());
    }

    @Test
    public void testBranch() {
        SwiftCode branch = new SwiftCode("LOSOWOSC", "BRANCH ADDRESS", "BANK NAME", "PL", "POLAND");

        when(this.swiftCodeRepository.findById(branch.getSwiftCode())).thenReturn(Optional.of(branch));

        Optional<SwiftCodeResponse> swiftCodeResponseOpt = this.swiftCodeService.getCodeDetails(branch.getSwiftCode());

        assertTrue(swiftCodeResponseOpt.isPresent());
        assertFalse(swiftCodeResponseOpt.get().isHeadquarter());
        assertEquals("POLAND", swiftCodeResponseOpt.get().getCountryName());
    }

    @Test
    public void testBranchInHeadquarter() {
        Optional<SwiftCodeResponse> headquarterOpt = this.getHeadquarter();
        assertTrue(headquarterOpt.isPresent());

        List<SwiftCodeResponse> branches = headquarterOpt.get().getBranches();
        assertFalse(branches.getFirst().isHeadquarter());
        assertNull(branches.getFirst().getCountryName());
    }

    private Optional<SwiftCodeResponse> getHeadquarter() {
        SwiftCode headQuarter = new SwiftCode("12345678XXX", "HEADQUARTER ADDRESS", "BANK NAME", "PL", "POLAND");
        SwiftCode branch = new SwiftCode("12345678", "BRANCH ADDRESS", "BANK NAME", "PL", "POLAND");

        when(this.swiftCodeRepository.findById(headQuarter.getSwiftCode())).thenReturn(Optional.of(headQuarter));
        when(this.swiftCodeRepository.findBySwiftCodeStartingWith(branch.getSwiftCode())).thenReturn(List.of(branch));

        return this.swiftCodeService.getCodeDetails(headQuarter.getSwiftCode());
    }

//    GET CODES BY COUNTRY

    @Test
    public void testISO2CodeLength() {
        assertThrows(IncorrectIso2Code.class, () -> this.swiftCodeService.getCodesByCountry("X"));
        assertThrows(IncorrectIso2Code.class, () -> this.swiftCodeService.getCodesByCountry("XYZ"));
    }

    @Test
    public void testCodesByCountry() {
        SwiftCode code1 = new SwiftCode("12345678", "BRANCH ADDRESS", "BANK NAME", "PL", "POLAND");
        SwiftCode code2 = new SwiftCode("12345670", "BRANCH ADDRESS", "BANK NAME", "PL", "POLAND");

        when(this.swiftCodeRepository.findByCountryISO2("PL")).thenReturn(List.of(code1, code2));

        Optional<CountryResponse> responseOpt = this.swiftCodeService.getCodesByCountry("PL");
        assertTrue(responseOpt.isPresent());
        CountryResponse response = responseOpt.get();
        assertEquals("PL", response.getCountryISO2());
        assertEquals("POLAND", response.getCountryName());
        assertEquals(2, response.getSwiftCodes().size());
    }

    @Test
    public void testEmptyCodesListByCountry() {
        when(this.swiftCodeRepository.findByCountryISO2("XX")).thenReturn(List.of());
        assertTrue(this.swiftCodeService.getCodesByCountry("XX").isEmpty());
    }

}