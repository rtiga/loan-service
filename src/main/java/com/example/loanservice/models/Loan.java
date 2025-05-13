package com.example.loanservice.models;

import com.example.loanservice.models.dto.LoanRequest;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String borrowerId;
    private BigDecimal principal;
    private BigDecimal rate;
    private BigDecimal roi;
    private String agreementLink;

    @Enumerated(EnumType.STRING)
    private LoanState state = LoanState.PROPOSED;

    private LocalDateTime approvalDate;
    private String approvalPhoto;
    private String validatorEmployeeId;
    private LocalDateTime disbursementDate;
    private String disbursementProof;
    private String disbursementEmployeeId;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Investment> investments;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum LoanState {
        PROPOSED, APPROVED, INVESTED, DISBURSED
    }

    public Loan() {
    }

    public Loan(LoanRequest loanRequest) {
        this.borrowerId = loanRequest.getBorrowerId();
        this.principal = loanRequest.getPrincipal();
        this.rate = loanRequest.getRate();
        this.roi = loanRequest.getRoi();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal roi) {
        this.roi = roi;
    }

    public String getAgreementLink() {
        return agreementLink;
    }

    public void setAgreementLink(String agreementLink) {
        this.agreementLink = agreementLink;
    }

    public LoanState getState() {
        return state;
    }

    public void setState(LoanState state) {
        this.state = state;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalPhoto() {
        return approvalPhoto;
    }

    public void setApprovalPhoto(String approvalPhoto) {
        this.approvalPhoto = approvalPhoto;
    }

    public String getValidatorEmployeeId() {
        return validatorEmployeeId;
    }

    public void setValidatorEmployeeId(String validatorEmployeeId) {
        this.validatorEmployeeId = validatorEmployeeId;
    }

    public LocalDateTime getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDateTime disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public String getDisbursementProof() {
        return disbursementProof;
    }

    public void setDisbursementProof(String disbursementProof) {
        this.disbursementProof = disbursementProof;
    }

    public String getDisbursementEmployeeId() {
        return disbursementEmployeeId;
    }

    public void setDisbursementEmployeeId(String disbursementEmployeeId) {
        this.disbursementEmployeeId = disbursementEmployeeId;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(List<Investment> investments) {
        this.investments = investments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}