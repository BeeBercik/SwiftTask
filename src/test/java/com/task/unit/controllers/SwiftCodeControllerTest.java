package com.task.unit.controllers;

import com.task.controllers.impl.SwiftCodeController;
import com.task.dto.CountrySwiftCodesResponse;
import com.task.dto.SwiftCodeResponse;
import com.task.services.impl.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(SwiftCodeController.class)
class SwiftCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SwiftCodeService swiftCodeService;

    @Test
    public void testGetSwiftCodeDetails_IfNotFound() throws Exception {
        when(this.swiftCodeService.getCodeDetails("12345678")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/v1/swift-codes/12345678"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Swift code not exists"));
    }

    @Test
    public void testGetSwiftCodeDetails_IfExistsSuccess() throws Exception {
        when(this.swiftCodeService.getCodeDetails("12345678")).thenReturn(Optional.of(new SwiftCodeResponse("ADDRESS", "BANK NAME", "PL", "POLAND", false, "12345678")));

        this.mockMvc.perform(get("/v1/swift-codes/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("12345678"))
                .andExpect(jsonPath("$.countryName").value("POLAND"));
    }

    @Test
    public void testGetAllSwiftCodesByCountry_IfNoResults() throws Exception {
        when(this.swiftCodeService.getCodesByCountry("XX")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/v1/swift-codes/country/XX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Swift codes with such country ISO code"));
    }

    @Test
    public void testGetAllSwiftCodesByCountry_IfSuccess() throws Exception {
        when(this.swiftCodeService.getCodesByCountry("PL")).thenReturn(
                Optional.of(new CountrySwiftCodesResponse(
                        "PL",
                        "POLAND",
                        List.of(new SwiftCodeResponse(
                                "ADDRESS",
                                "BANK NAME",
                                "PL",
                                "POLAND",
                                false,
                                "12345678"))))
        );

        this.mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("POLAND"));
    }

    @Test
    public void testAddNewSwiftCode_IfAlreadyExists() throws Exception {
        when(this.swiftCodeService.addNewSwiftCode(any())).thenReturn(false);

        String json = """
                {
                  "address": "ADDRESS",
                  "bankName": "BANK NAME",
                  "countryISO2": "PL",
                  "countryName": "POLAND",
                  "isHeadquarter": false,
                  "swiftCode": "12345678"
                }
                """;

        this.mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Swift code already exists"));
    }

    @Test
    public void testAddNewSwiftCode_IfSuccess() throws Exception {
        when(this.swiftCodeService.addNewSwiftCode(any())).thenReturn(true);

        String json = """
                {
                  "address": "ADDRESS",
                  "bankName": "BANK NAME",
                  "countryISO2": "PL",
                  "countryName": "POLAND",
                  "isHeadquarter": false,
                  "swiftCode": "12345678"
                }
                """;

        this.mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code added"));
    }

    @Test
    public void testDeleteSwiftCode_IfNotExist() throws Exception {
        when(this.swiftCodeService.deleteSwiftCode("12345678")).thenReturn(false);

        this.mockMvc.perform(delete("/v1/swift-codes/12345678"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Swift code not exists"));
    }

    @Test
    public void testDeleteSwiftCode_IfSuccess() throws Exception {
        when(this.swiftCodeService.deleteSwiftCode("12345678")).thenReturn(true);

        this.mockMvc.perform(delete("/v1/swift-codes/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code deleted"));
    }
}