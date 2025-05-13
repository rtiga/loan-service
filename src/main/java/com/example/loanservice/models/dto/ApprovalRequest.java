package com.example.loanservice.models.dto;

public class ApprovalRequest {
    private String photo;
    private String employeeId;

    public ApprovalRequest() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
