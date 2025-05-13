package com.example.loanservice.services;

import com.example.loanservice.models.*;
import com.example.loanservice.models.repositories.InvestmentRepository;
import com.example.loanservice.models.repositories.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private LoanService loanService;
    private LoanRepository loanRepo;
    private InvestmentRepository investmentRepo;

    @BeforeEach
    void setUp() {
        loanRepo = Mockito.mock(LoanRepository.class);
        investmentRepo = Mockito.mock(InvestmentRepository.class);
        loanService = new LoanService(loanRepo, investmentRepo);
    }

    @Test
    void testCreateLoan() {
        Loan loan = new Loan();
        loan.setBorrowerId("123");
        loan.setPrincipal(BigDecimal.valueOf(10000));

        when(loanRepo.save(any())).thenReturn(loan);
        Loan created = loanService.createLoan(loan);

        assertEquals("123", created.getBorrowerId());
    }

    @Test
    void testInvestmentExceedsPrincipal() {
        Loan loan = new Loan();
        loan.setState(Loan.LoanState.APPROVED);
        loan.setPrincipal(BigDecimal.valueOf(1000));
        loan.setInvestments(List.of());

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalArgumentException.class, () -> {
            loanService.addInvestment(1L, "investor1", BigDecimal.valueOf(1500));
        });
    }

    @Test
    void testDisburseNonInvestedLoanThrows() {
        Loan loan = new Loan();
        loan.setState(Loan.LoanState.APPROVED);

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));

        assertThrows(IllegalStateException.class, () -> {
            loanService.disburseLoan(1L, "proof.jpg", "emp123");
        });
    }

    @Test
    void testApproveLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setState(Loan.LoanState.PROPOSED);

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<Loan> result = loanService.approveLoan(1L, "photo.png", "emp001");

        assertTrue(result.isPresent());
        assertEquals(Loan.LoanState.APPROVED, result.get().getState());
        assertEquals("photo.png", result.get().getApprovalPhoto());
        assertEquals("emp001", result.get().getValidatorEmployeeId());
        assertNotNull(result.get().getApprovalDate());
    }

    @Test
    void testDisburseLoanSuccess() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setState(Loan.LoanState.INVESTED);

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<Loan> result = loanService.disburseLoan(1L, "proof.jpg", "emp123");

        assertTrue(result.isPresent());
        assertEquals(Loan.LoanState.DISBURSED, result.get().getState());
        assertEquals("proof.jpg", result.get().getDisbursementProof());
        assertEquals("emp123", result.get().getDisbursementEmployeeId());
        assertNotNull(result.get().getDisbursementDate());
    }
}

