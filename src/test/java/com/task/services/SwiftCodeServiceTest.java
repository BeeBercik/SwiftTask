package com.task.services;

import com.task.dto.CountryResponse;
import com.task.dto.SwiftCodeRequest;
import com.task.dto.SwiftCodeResponse;
import com.task.exceptions.IncorrectIso2Code;
import com.task.exceptions.IncorrectSwiftCode;
import com.task.exceptions.IncorrectSwiftCodeRequest;
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
    public void testGetCodeDetails_IfIncorrectCode() {
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("123456789"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("12345XXX"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.getCodeDetails("12345678901"));
    }

    @Test
    public void testGetCodeDetails_IfNotExists() {
        when(this.swiftCodeRepository.findById("12345678")).thenReturn(Optional.empty());

        assertTrue(this.swiftCodeService.getCodeDetails("12345678").isEmpty());
    }

    @Test
    public void testGetCodeDetails_Headquarter() {
        Optional<SwiftCodeResponse> headquarterOpt = this.getHeadquarter();

        assertTrue(headquarterOpt.isPresent());
        SwiftCodeResponse headquarter = headquarterOpt.get();
        assertTrue(headquarter.isHeadquarter());
        assertEquals(1, headquarter.getBranches().size());
    }

    @Test
    public void testGetCodeDetails_Branch() {
        SwiftCode branch = new SwiftCode("LOSOWOSC", "BRANCH ADDRESS", "BANK NAME", "PL", "POLAND");

        when(this.swiftCodeRepository.findById(branch.getSwiftCode())).thenReturn(Optional.of(branch));

        Optional<SwiftCodeResponse> swiftCodeResponseOpt = this.swiftCodeService.getCodeDetails(branch.getSwiftCode());

        assertTrue(swiftCodeResponseOpt.isPresent());
        assertFalse(swiftCodeResponseOpt.get().isHeadquarter());
        assertEquals("POLAND", swiftCodeResponseOpt.get().getCountryName());
    }

    @Test
    public void testGetCodeDetails_BranchInHeadquarter() {
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

    @Test
    public void testGetCodesByCountry_IfIsoCodeIncorrect() {
        assertThrows(IncorrectIso2Code.class, () -> this.swiftCodeService.getCodesByCountry("X"));
        assertThrows(IncorrectIso2Code.class, () -> this.swiftCodeService.getCodesByCountry("XYZ"));
    }

    @Test
    public void testGetCodesByCountry_IfCorrect() {
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
    public void testGetCodesByCountry_IfEmptyResultList() {
        when(this.swiftCodeRepository.findByCountryISO2("XX")).thenReturn(List.of());
        assertTrue(this.swiftCodeService.getCodesByCountry("XX").isEmpty());
    }

    @Test
    public void testAddNewSwiftCode_IfExists() {
        when(this.swiftCodeRepository.existsBySwiftCode("12345678")).thenReturn(true);

        SwiftCodeRequest swiftCodeRequest = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", false, "12345678");

        assertFalse(this.swiftCodeService.addNewSwiftCode(swiftCodeRequest));
    }

    @Test
    public void testAddNewSwiftCode_IfIncorrectSwiftCode() {
        SwiftCodeRequest swiftCodeRequest1 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", true, "12345678");
        SwiftCodeRequest swiftCodeRequest2 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", false, "12345678XXX");
        SwiftCodeRequest swiftCodeRequest3 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", false, "12345");
        SwiftCodeRequest swiftCodeRequest4 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", true, "1234567890");

        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.addNewSwiftCode(swiftCodeRequest1));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.addNewSwiftCode(swiftCodeRequest2));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.addNewSwiftCode(swiftCodeRequest3));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.addNewSwiftCode(swiftCodeRequest4));
    }

    @Test
    public void testAddNewSwiftCode_IfInvalidateSwiftCodeRequestFields() {
        SwiftCodeRequest invalidRequest1 = new SwiftCodeRequest("", "BANK NAME", "PL", "POLAND", false, "12345678");
        SwiftCodeRequest invalidRequest2 = new SwiftCodeRequest("ADDRESS", "", "PL", "POLAND", false, "12345678");
        SwiftCodeRequest invalidRequest3 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "P", "POLAND", false, "12345678");
        SwiftCodeRequest invalidRequest4 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "", false, "12345678");
        SwiftCodeRequest invalidRequest5 = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", null, "12345678");

        assertThrows(IncorrectSwiftCodeRequest.class, () -> this.swiftCodeService.addNewSwiftCode(invalidRequest1));
        assertThrows(IncorrectSwiftCodeRequest.class, () -> this.swiftCodeService.addNewSwiftCode(invalidRequest2));
        assertThrows(IncorrectSwiftCodeRequest.class, () -> this.swiftCodeService.addNewSwiftCode(invalidRequest3));
        assertThrows(IncorrectSwiftCodeRequest.class, () -> this.swiftCodeService.addNewSwiftCode(invalidRequest4));
        assertThrows(IncorrectSwiftCodeRequest.class, () -> this.swiftCodeService.addNewSwiftCode(invalidRequest5));
    }

    @Test
    public void testAddNewSwiftCode_IfCorrect() {
        when(this.swiftCodeRepository.existsBySwiftCode("12345678")).thenReturn(false);

        SwiftCodeRequest swiftCodeRequest = new SwiftCodeRequest("ADDRESS", "BANK NAME", "PL", "POLAND", false, "12345678");

        assertTrue(this.swiftCodeService.addNewSwiftCode(swiftCodeRequest));
    }

    @Test
    public void testDeleteSwiftCode_IfNotExist() {
        when(this.swiftCodeRepository.findById("12345678")).thenReturn(Optional.empty());
        assertFalse(this.swiftCodeService.deleteSwiftCode("12345678"));
    }

    @Test
    public void testDeleteSwiftCode_IfIncorrectCode() {
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.deleteSwiftCode("123456789"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.deleteSwiftCode("12345XXX"));
        assertThrows(IncorrectSwiftCode.class, () -> this.swiftCodeService.deleteSwiftCode("12345678901"));
    }

    @Test
    public void testDeleteSwiftCode_IfCorrect() {
        SwiftCode swiftCode = new SwiftCode("12345678", "ADDRESS", "BANK NAME", "PL", "POLAND");

        when(this.swiftCodeRepository.findById("12345678")).thenReturn(Optional.of(swiftCode));
        assertTrue(this.swiftCodeService.deleteSwiftCode("12345678"));
    }
}