package com.example.loanservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
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
}