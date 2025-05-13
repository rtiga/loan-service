package com.example.loanservice.controllers;

import com.example.loanservice.models.Loan;
import com.example.loanservice.models.dto.ApprovalRequest;
import com.example.loanservice.models.dto.DisburseRequest;
import com.example.loanservice.models.dto.InvestRequest;
import com.example.loanservice.models.dto.LoanRequest;
import com.example.loanservice.services.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanRequest body) {
        return ResponseEntity.ok(loanService.createLoan(new Loan(body)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
        return loanService.getLoan(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Loan> approveLoan(@PathVariable Long id, @RequestBody ApprovalRequest body) {
        return loanService.approveLoan(id, body.getPhoto(), body.getEmployeeId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/invest")
    public ResponseEntity<Loan> invest(@PathVariable Long id, @RequestBody InvestRequest body) {
        return loanService.addInvestment(id, body.getInvestorId(), body.getAmount() )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/disburse")
    public ResponseEntity<Loan> disburse(@PathVariable Long id, @RequestBody DisburseRequest body) {
        return loanService.disburseLoan(id, body.getProof(), body.getEmployeeId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
