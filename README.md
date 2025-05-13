# Loan Service
This codebase is for assignment purposes only

we are building a loan engine. A loan can a multiple state: proposed , approved, invested, disbursed. the rule of state:
1. proposed is the initial state (when loan created it will has proposed state):
2. approved is once it approved by our staff.
   a. a approval must contains several information:
   i. the picture proof of the a field validator has visited the borrower
   ii. the employee id of field validator
   iii. date of approval
   b. once approved it can not go back to proposed state
   c. once approved loan is ready to be offered to investors/lender
3. invested is once total amount of invested is equal the loan principal
   a. loan can have multiple investors, each with each their own amount
   b. total of invested amount can not be bigger than the loan principal amount
   c. once invested all investors will receive an email containing link to agreement letter (pdf)
4. disbursed is when is loan is given to borrower.
   a. a disbursement must contains several information:
   i. the loan agreement letter signed by borrower (pdf/jpeg)
   ii. the employee id of the field officer that hands the money and/or collect the agreement letter
   iii. date of disbursement
   movement between state can only move forward, and a loan only need following information:
   borrower id number
   principal amount
   rate, will define total interest that borrower will pay
   ROI return of investment, will define total profit received by investors
   link to the generated agreement letter
   design a RESTFful api that satisfy above requirement

## Assumptions Made
- Photos, proof, and/or files are already uploaded, so it only receives link to the file
- ROI is calculated by the Lender

## Room for enhancement / improvement
- Check for the users: lender/borrower, employee that approve the loan, employee that disburse the loan
- Duration of the Loan
- Loan status (inquiry) after it is disbursed

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

## API Doc
```
http://localhost:8080/swagger-ui/index.html
```

## Setup
```bash
./mvnw spring-boot:run
```

## Tests
```bash
./mvnw test
```

