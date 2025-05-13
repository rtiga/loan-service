package com.example.loanservice.models.dto;



import java.math.BigDecimal;

public class LoanRequest {

    private String borrowerId;
    private BigDecimal principal;
    private BigDecimal rate;
    private BigDecimal roi;

    public LoanRequest() {
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
}
