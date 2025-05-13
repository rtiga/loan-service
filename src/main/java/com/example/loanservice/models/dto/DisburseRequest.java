package com.example.loanservice.models.dto;

public class DisburseRequest {

    private String proof;
    private String employeeId;

    public DisburseRequest() {
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
