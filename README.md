# Bank Application - Secure REST APIs with JWT Authentication

### 🚀 Overview
This is a Spring Boot-based Bank Application that provides REST APIs for:
* Creating a new bank account (email notification on success)
* Performing credit and debit transactions (real-time email alerts)
* Transferring funds between accounts (email notifications for both sender and receiver)
* Secure authentication using Spring Security with JWT

### 🔑 Features
* Create Account with Email Notification
* Secure Authentication with JWT
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
Method	Endpoint	Description	Authorization
POST	/api/auth/register	Register a new user	❌ Not Required
POST	/api/auth/login	Login and get JWT Token	❌ Not Required
##### Bank Account APIs
Method	Endpoint	Description	Authorization
POST	/api/accounts/create	Create new bank account	✅ Requires JWT
GET	/api/accounts/{id}	Get account details	✅ Requires JWT
##### Transaction APIs
Method	Endpoint	Description	Authorization
POST	/api/transactions/credit	Credit money into an account	✅ Requires JWT
POST	/api/transactions/debit	Debit money from an account	✅ Requires JWT
POST	/api/transactions/transfer	Transfer money between accounts	✅ Requires JWT
