package com.example.loanservice.services;

import com.example.loanservice.controllers.LoanController;
import com.example.loanservice.models.Loan;
import com.example.loanservice.models.LoanState;
import com.example.loanservice.models.repositories.InvestmentRepository;
import com.example.loanservice.models.repositories.LoanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
public class LoanServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @MockBean
    private LoanRepository loanRepo;

    @MockBean
    private InvestmentRepository investmentRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Loan sampleLoan;

    @BeforeEach
    void setup() {
        sampleLoan = new Loan();
        sampleLoan.setId(1L);
        sampleLoan.setBorrowerId("123");
        sampleLoan.setPrincipal(BigDecimal.valueOf(1000));
        sampleLoan.setState(LoanState.PROPOSED);
    }

    @Test
    void testApproveLoanApi() throws Exception {
        sampleLoan.setState(LoanState.APPROVED);

        when(loanService.approveLoan(eq(1L), eq("photo.jpg"), eq("emp001"))).thenReturn(Optional.of(sampleLoan));

        mockMvc.perform(post("/loans/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"approvalPhoto\":\"photo.jpg\"," +
                                "\"validatorEmployeeId\":\"emp001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("APPROVED"));
    }

    @Test
    void testDisburseLoanApi() throws Exception {
        sampleLoan.setState(LoanState.DISBURSED);

        when(loanService.disburseLoan(eq(1L), eq("signed.jpg"), eq("emp009"))).thenReturn(Optional.of(sampleLoan));

        mockMvc.perform(post("/loans/1/disburse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"disbursementProof\":\"signed.jpg\"," +
                                "\"disbursementEmployeeId\":\"emp009\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("DISBURSED"));
    }

    @Test
    void testApproveLoanNotFound() throws Exception {
        when(loanService.approveLoan(anyLong(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/loans/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"approvalPhoto\":\"photo.jpg\"," +
                                "\"validatorEmployeeId\":\"emp001\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddInvestmentApi() throws Exception {
        sampleLoan.setState(LoanState.INVESTED);

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
                .andExpect(content().string("Investment exceeds loan principal"));
    }
}

