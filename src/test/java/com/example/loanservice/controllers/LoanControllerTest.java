package com.example.loanservice.controllers;

import com.example.loanservice.models.Loan;
import com.example.loanservice.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan sampleLoan;

    @BeforeEach
    void setup() {
        sampleLoan = new Loan();
        sampleLoan.setId(1L);
        sampleLoan.setBorrowerId("123");
        sampleLoan.setPrincipal(BigDecimal.valueOf(1000));
        sampleLoan.setState(Loan.LoanState.PROPOSED);
    }

    @Test
    void testApproveLoanApi() throws Exception {
        sampleLoan.setState(Loan.LoanState.APPROVED);

        when(loanService.approveLoan(eq(1L), eq("photo.jpg"), eq("emp001"))).thenReturn(Optional.of(sampleLoan));

        mockMvc.perform(post("/loans/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"photo\":\"photo.jpg\"," +
                                "\"employeeId\":\"emp001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("APPROVED"));
    }

    @Test
    void testApproveLoanApiIllegalState() throws Exception {
        when(loanService.addInvestment(eq(1L), eq("investor002"), eq(BigDecimal.valueOf(2000))))
                .thenThrow(new IllegalStateException("Only proposed loans can be approved"));

        mockMvc.perform(post("/loans/1/invest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"investorId\":\"investor002\"," +
                                "\"amount\":2000}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Only proposed loans can be approved"));
    }

    @Test
    void testDisburseLoanApi() throws Exception {
        sampleLoan.setState(Loan.LoanState.DISBURSED);

        when(loanService.disburseLoan(eq(1L), eq("signed.jpg"), eq("emp009"))).thenReturn(Optional.of(sampleLoan));

        mockMvc.perform(post("/loans/1/disburse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"proof\":\"signed.jpg\"," +
                                "\"employeeId\":\"emp009\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("DISBURSED"));
    }

    @Test
    void testDisburseLoanApiIllegalState() throws Exception {
        sampleLoan.setState(Loan.LoanState.DISBURSED);

        when(loanService.disburseLoan(eq(1L), eq("signed.jpg"), eq("emp009")))
                .thenThrow(new IllegalStateException("Only invested loans can be disbursed"));

        mockMvc.perform(post("/loans/1/disburse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"proof\":\"signed.jpg\"," +
                                "\"employeeId\":\"emp009\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Only invested loans can be disbursed"));
    }

    @Test
    void testApproveLoanNotFound() throws Exception {
        when(loanService.approveLoan(anyLong(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/loans/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"photo\":\"photo.jpg\"," +
                                "\"employeeId\":\"emp001\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddInvestmentApi() throws Exception {
        sampleLoan.setState(Loan.LoanState.INVESTED);

        when(loanService.addInvestment(eq(1L), eq("investor001"), eq(BigDecimal.valueOf(500))))
                .thenReturn(Optional.of(sampleLoan));

        mockMvc.perform(post("/loans/1/invest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"investorId\":\"investor001\"," +
                                "\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("INVESTED"));
    }

    @Test
    void testAddInvestmentLoanNotFound() throws Exception {
        when(loanService.addInvestment(anyLong(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/loans/1/invest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"investorId\":\"investor001\"," +
                                "\"amount\":500}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddInvestmentExceedingPrincipal() throws Exception {
        when(loanService.addInvestment(eq(1L), eq("investor002"), eq(BigDecimal.valueOf(2000))))
                .thenThrow(new IllegalArgumentException("Investment exceeds loan principal"));

        mockMvc.perform(post("/loans/1/invest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"investorId\":\"investor002\"," +
                                "\"amount\":2000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Investment exceeds loan principal"));
    }
}
