# Bank Application - Secure REST APIs with JWT Authentication

### 🚀 Overview
This is a Spring Boot-based Bank Application that provides REST APIs for:
* Creating a new bank account (email notification on success)
* Performing credit and debit transactions (real-time email alerts)
* Transferring funds between accounts (email notifications for both sender and receiver)
* Statement Generation for account transaction history (real-time email notifications for generated statements)
* Secure authentication using Spring Security with JWT

### 🔑 Features
* Create Account with Email Notification
* Secure Authentication with JWT
* Statement Generation with Email Notification for Account Transaction History
* Credit, Debit, and Transfer Funds with Real-Time Email Alerts
* Spring Security for Role-Based Access
* RESTful APIs for Bank Operations
* Exception Handling and Custom Error Responses

### 🛠️ Technology Stack
* Backend: Java, Spring Boot, Spring Security, JWT
* Database: MySQL
* Security: Spring Security, JWT Authentication
* Email Service: JavaMailSender
* Testing: Postman
* Develoment : Intellij

 ### API Endpoints
##### Authentication APIs
* POST	/api/user	Create new account. (Auth ❌ Not Required)
* POST	/api/login	Login and get JWT Token. (Auth 	❌ Not Required)

##### Bank Account APIs
* GET	/api/user/name	 Get Account Name. (✅ Requires JWT)
* GET	/api/user/balance	Get Balance details. 	(✅ Requires JWT)

##### Transaction APIs
* POST	/api/user/credit	Credit money into an account. (✅ Requires JWT)
* POST	/api/user/debit	Debit money from an account.	(✅ Requires JWT)
* POST	/api/user/transfer	Transfer money between accounts.	(✅ Requires JWT)

##### Statement Generation API
* GET /api/user/statement – Get Account Statement with transaction history. (✅ Requires JWT, Email Notification Sent)

### 🏗️ Project Setup
###### Clone the Repository
  * `git clone https://github.com/deanraaj/Bank-Application.git`
  * `cd Bank-Application`

### Configure Database
* Update application.properties with your MySQL database credentials:
properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```
### Run the Application (bash)
`mvn spring-boot:run`

### 📧 Email Notifications
Users receive real-time email notifications for:
* ✅ Account Creation
* ✅ Credit Transactions
* ✅ Debit Transactions
* ✅ Fund Transfers
* ✅ Statement Generation
