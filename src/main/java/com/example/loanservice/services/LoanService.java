package com.example.loanservice.services;

import com.example.loanservice.models.Investment;
import com.example.loanservice.models.Loan;
import com.example.loanservice.models.repositories.InvestmentRepository;
import com.example.loanservice.models.repositories.LoanRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final InvestmentRepository investmentRepo;

    public LoanService(LoanRepository loanRepo, InvestmentRepository investmentRepo) {
        this.loanRepo = loanRepo;
        this.investmentRepo = investmentRepo;
    }

    public Loan createLoan(Loan loan) {
        loan.setState(Loan.LoanState.PROPOSED);
        return loanRepo.save(loan);
    }

    public Optional<Loan> getLoan(Long id) {
        return loanRepo.findById(id);
    }

    public Optional<Loan> approveLoan(Long id, String photo, String employeeId) {
        return loanRepo.findById(id).map(loan -> {
            if (loan.getState() != Loan.LoanState.PROPOSED) throw new IllegalStateException("Only proposed loans can be approved");
            loan.setState(Loan.LoanState.APPROVED);
            loan.setApprovalDate(LocalDateTime.now());
            loan.setApprovalPhoto(photo);
            loan.setValidatorEmployeeId(employeeId);
            return loanRepo.save(loan);
        });
    }

    public Optional<Loan> addInvestment(Long loanId, String investorId, BigDecimal amount) {
        return loanRepo.findById(loanId).map(loan -> {
            if (loan.getState() != Loan.LoanState.APPROVED)
                throw new IllegalStateException("Investments can only be made on approved loans");

            BigDecimal totalInvested = loan.getInvestments().stream().map(Investment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalInvested.add(amount).compareTo(loan.getPrincipal()) > 0)
                throw new IllegalArgumentException("Investment exceeds loan principal");

            Investment investment = new Investment();
            investment.setLoan(loan);
            investment.setInvestorId(investorId);
            investment.setAmount(amount);
            investmentRepo.save(investment);

            totalInvested = totalInvested.add(amount);
            if (totalInvested.compareTo(loan.getPrincipal()) == 0) {
                loan.setState(Loan.LoanState.INVESTED);
                loan.setAgreementLink("http://example.com/agreement/" + loan.getId());
            }

            return loanRepo.save(loan);
        });
    }

    public Optional<Loan> disburseLoan(Long id, String proof, String employeeId) {
        return loanRepo.findById(id).map(loan -> {
            if (loan.getState() != Loan.LoanState.INVESTED)
                throw new IllegalStateException("Only invested loans can be disbursed");
            loan.setState(Loan.LoanState.DISBURSED);
            loan.setDisbursementDate(LocalDateTime.now());
            loan.setDisbursementProof(proof);
            loan.setDisbursementEmployeeId(employeeId);
            return loanRepo.save(loan);
        });
    }
}
