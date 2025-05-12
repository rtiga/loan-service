package com.example.loanservice.models.repositories;

import com.example.loanservice.models.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
}
