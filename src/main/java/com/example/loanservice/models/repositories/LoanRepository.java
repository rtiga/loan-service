package com.example.loanservice.models.repositories;

import com.example.loanservice.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
