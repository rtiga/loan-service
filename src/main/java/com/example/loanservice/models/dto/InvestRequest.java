package com.example.loanservice.models.dto;

import java.math.BigDecimal;

public class InvestRequest {

    private String investorId;
    private BigDecimal amount;

    public InvestRequest() {
    }

    public String getInvestorId() {
        return investorId;
    }

    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
