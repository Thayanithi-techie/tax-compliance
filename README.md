# Tax Gap Detection & Compliance Validation Service

## Project Overview
The Tax Gap Detection & Compliance Validation Service is a backend system designed to help tax auditors validate financial transactions submitted by customers.  
It recalculates expected tax, detects tax gaps, applies configurable compliance rules, generates exceptions, maintains audit logs, and provides analytical reports.

This project is built using **Java 17, Spring Boot, JPA, and MySQL**, following enterprise backend design practices.

---

## Business Problem
Tax authorities need a system to:
- Validate large batches of financial transactions
- Detect underpaid and overpaid taxes
- Apply rule-based tax compliance checks
- Maintain audit trails for regulatory review
- Generate customer-wise and exception-wise reports

This application automates all of the above.

---

### Architecture Layers
- Controller Layer: Handles REST APIs and request validation
- Service Layer: Contains core business logic such as tax calculation and compliance checks
- Rule Engine Layer: Executes database-driven compliance rules using the Strategy Pattern
- Repository Layer: Manages persistence using Spring Data JPA
- Database Layer: MySQL stores transactional, rule, exception, and audit data

### Rule Engine Design
Compliance rules are stored in the database with JSON-based configuration. Each rule is implemented as a separate strategy class, allowing new rules to be added without modifying existing logic. Rules can be enabled or disabled dynamically without redeployment.

### Audit Logging
Every critical operation (transaction ingestion, tax computation, rule execution) generates an immutable audit log entry to ensure traceability and regulatory compliance.

### Reporting Strategy
Reports are generated using SQL aggregation queries to ensure efficiency and avoid loading large datasets into memory.

This design closely resembles real-world financial and regulatory systems where accuracy, traceability, and configurability are critical.

---

## Technology Stack

| Category | Technology |
|--------|------------|
| Language | Java 17 |
| Framework | Spring Boot |
| ORM | Spring Data JPA (Hibernate) |
| Database | MySQL |
| Validation | Jakarta Bean Validation |
| JSON | Jackson |
| Testing | JUnit 5, Mockito |
| Build Tool | Maven |

---

## Package Structure

com.company.tax_compliance
├── controller
├── service
│ ├── rule
│ │ └── impl
│ └── report
├── repository
├── domain
│ ├── entity
│ └── enums
├── dto
├── audit
├── exception
├── config
└── TaxComplianceApplication.java


---

## Database Tables
- **transactions** – Stores raw transactions, validation status, and tax computation
- **tax_rules** – Stores configurable compliance rules in JSON format
- **exceptions** – Stores compliance and rule violations
- **audit_logs** – Stores audit events (ingestion, tax computation, rule execution)

---

## Core Features

### 1. Transaction Upload & Validation
- Batch transaction upload REST API
- Bean validation for required fields and data correctness
- Invalid transactions logged with failure reason
- Validation status stored in database

---

### 2. Tax Gap Calculation Engine
For each valid transaction:
expectedTax = amount × taxRate
taxGap = expectedTax − reportedTax


**Compliance Classification**

| Condition | Status |
|--------|--------|
| reportedTax missing | NON_COMPLIANT |
| taxGap ≤ 1 | COMPLIANT |
| taxGap > 1 | UNDERPAID |
| taxGap < -1 | OVERPAID |

---

### 3. Database-Driven Rule Engine
Compliance rules are:
- Stored in MySQL
- Configured using JSON
- Enabled or disabled without redeployment

**Implemented Rules**
1. High-Value Transaction Rule
2. Refund Validation Rule
3. GST Slab Violation Rule

**Design Pattern Used:** Strategy Pattern

---

### 4. Exception Management
- Rule violations create exception records
- Exceptions can be filtered by customer, severity, and rule name
- Designed to support auditor investigations

---

### 5. Audit Logging
Every critical operation generates an audit log:
- INGESTION
- TAX_COMPUTATION
- RULE_EXECUTION

This ensures traceability and compliance.

---

### 6. Reporting APIs

#### Customer Tax Summary Report
- Total transaction amount
- Total reported tax
- Total expected tax
- Total tax gap
- Compliance score

#### Exception Summary Report
- Total exceptions
- Severity-wise exception count
- Customer-wise exception count

Reports are generated using SQL aggregation queries for performance.

---

## REST API Endpoints

### Transaction Upload
POST /api/transactions/upload

### Exception APIs
GET /api/exceptions
GET /api/exceptions/customer/{customerId}
GET /api/exceptions/severity/{severity}
GET /api/exceptions/rule/{ruleName}


---

## Testing Strategy
- Unit tests focus on business logic (services and rule engine)
- Implemented using JUnit 5 and Mockito
- Controllers tested via Postman
- Achieved approximately 40–50% unit test coverage

---

## How to Run the Application

### Prerequisites
- Java 17
- Maven
- MySQL

### Steps

--bash
git clone <github-repo-url>

cd tax-compliance

mvn clean spring-boot:run

Application runs on:
http://localhost:8080

## Database Setup Instructions

1. Start MySQL server
2. Create database:
CREATE DATABASE tax_compliance_db;

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/tax_compliance_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=validate


Run the provided SQL scripts to create tables:

transactions
tax_rules
exceptions
audit_logs

## Sample Transaction Upload JSON

```json
[
  {
    "transactionId": "TXN500",
    "date": "2026-01-12",
    "customerId": "CUST500",
    "amount": 150000,
    "taxRate": 0.18,
    "reportedTax": 25000,
    "transactionType": "SALE"
  }
]


