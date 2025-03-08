package com.task.integration.controllers;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SwiftCodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @Sql("/test-data.sql")
    public void tesGetCodeDetails_IfSuccess() throws Exception {
        this.mockMvc.perform(get("/v1/swift-codes/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("12345678"));
    }

    @Test
    public void tesGetCodeDetails_IfCodeNotExists() throws Exception {
        this.mockMvc.perform(get("/v1/swift-codes/12345678"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Swift code not exists"));
    }

    @Test
    public void testGetAllSwiftCodesByCountry_IfNoResults() throws Exception {
        this.mockMvc.perform(get("/v1/swift-codes/country/XX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Swift codes with such country ISO code"));
    }

    @Test
    @Sql("/test-data.sql")
    public void testGetAllSwiftCodesByCountry_IfSuccess() throws Exception {
        this.mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCodes").isArray())
                .andExpect(jsonPath("$.countryName").value("POLAND"));
    }

    @Test
    public void testAddNewSwiftCode_IfSuccess() throws Exception {
        String json = """
            {
              "address": "ADDRESS",
              "bankName": "BANK NAME",
              "countryISO2": "PL",
              "countryName": "Poland",
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
    @Sql("/test-data.sql")
    public void testAddNewSwiftCode_IfAlreadyExists() throws Exception {
        String json = """
            {
              "address": "ADDRESS",
              "bankName": "BANK NAME",
              "countryISO2": "PL",
              "countryName": "Poland",
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
    public void testDeleteSwiftCode_IfNotExist() throws Exception {
        this.mockMvc.perform(delete("/v1/swift-codes/12345678"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Swift code not exists"));
    }

    @Test
    @Sql("/test-data.sql")
    public void testDeleteSwiftCode_IfSuccess() throws Exception {
        this.mockMvc.perform(delete("/v1/swift-codes/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Swift code deleted"));
    }
}
