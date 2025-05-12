# Loan Service
This codebase is for assignment purposes only

## Tech Stack
- Java 17, Spring Boot
- MySQL, Spring Data JPA
- JUnit 5 for tests

## Loan States
- `proposed`: Initial state
- `approved`: After staff approval with validator photo and ID
- `invested`: Once fully funded by investors
- `disbursed`: After the loan is given to the borrower

## Endpoints
```
POST /loans                   - Create loan
POST /loans/{id}/approve      - Approve loan
POST /loans/{id}/invest       - Add investment
POST /loans/{id}/disburse     - Disburse loan
GET  /loans/{id}              - Get loan details
```

## Setup
```bash
./mvnw spring-boot:run
```

## Tests
```bash
./mvnw test
```

