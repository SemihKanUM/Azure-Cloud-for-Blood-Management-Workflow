# LargeScale – Blood Management Workflow Backend

This repository contains a **Java Spring Boot application** that was developed and deployed as part of a **Blood Management workflow system**.  
The project is cloud-ready and was deployed on **Microsoft Azure** to ensure scalability, reliability, and secure access.  

---

## ⚙️ Features
- Backend service built with **Spring Boot 3**  
- **REST APIs** to handle workflow operations for blood management  
- **Swagger / OpenAPI** documentation for API exploration  
- Data serialization with **Jackson**  
- **Logging and monitoring** with SLF4J and Micrometer  
- Deployed on **Azure Cloud** for scalability and production readiness  

---

## 🏥 Blood Management Workflow Use Case
The system supports key steps in the **blood management process**:  
- Registering and managing donors and recipients  
- Tracking donations and blood unit availability  
- Workflow automation for request/approval processes  
- Secure and scalable deployment via **Azure App Services**  

---

## 📂 Project Structure

LargeScale/
│── src/ # Main application source code
│── .idea/ # IntelliJ project configuration
│── pom.xml # Maven dependencies
│── README.md # Documentation


---

## 🚀 Getting Started

### Prerequisites
- Java 17+  
- Maven 3+  
- Azure CLI (if deploying to Azure)  

### Run Locally
```bash
mvn spring-boot:run

App will run on:
http://localhost:8080
