
## 🚀 Project Overview

**Food-Order-Application** is a demonstration of a production-style microservices architecture using modern Spring ecosystem tools.  
It showcases how to structure, configure, and connect multiple services in a scalable, resilient, and maintainable way — ideal for backend developers, microservices learning, or as a portfolio project.

### Key Features & Architecture Highlights

- A dedicated **Auth Service** with JWT-based authentication & authorization  
- **API Gateway** — a unified entry point for all client requests  
- **Service Discovery** via Eureka — enables dynamic registration & lookup of service instances  
- **Client-Side Load Balancing** using Spring Cloud LoadBalancer for horizontal scalability  
- **Inter-service communication** via Feign clients — clean, declarative REST calls between microservices  
- **Fault tolerance & resilience** using Resilience4j (Circuit Breaker, Retry, Bulkhead, Rate Limiting, Fallbacks)  
- **Centralized configuration** using Spring Cloud Config Server backed by a Git repo — externalized configs and easy environment switching  
- Modular microservices (e.g. User Service, Order Service, etc.) for separation of concerns  
- Clean folder/module structure for clarity and maintainability  

---

## 📂 Repository Structure (modules overview)

/ (root)
│
├── config-server/ # Centralized configuration for all services
├── gateway/ # API Gateway, routing & filters
├── auth-service/ # Authentication & authorization with JWT
├── user-service/ # User management, registration, etc.
├── order-service/ # Order processing / order management
├── server/ # for service registry and discovery
└── README.md # This file



## 🛠️ Prerequisites

- Java 17 (or the Java version you used)  
- Maven / Gradle (depending on your build tool)  
- Git  

---

## 📥 How to Run Locally

1. Clone the repository  
    ```bash
    git clone https://github.com/your-username/food-order-application.git
    cd food-order-application
    ```  
2. Start the Config Server first (if you’re using centralized config)  
    ```bash
    cd config-server
    mvn spring-boot:run
    ```  
3. Start the Service Discovery (Eureka) server  
    ```bash
    cd ../eureka-server  # or the module name in your repo
    mvn spring-boot:run
    ```  
4. Start the Gateway  
    ```bash
    cd ../gateway
    mvn spring-boot:run
    ```  
5. Start individual microservices (Auth, User, Order, etc.) — one by one or all together  
---

## 🧪 Usage / Example APIs

| Service / Module | Endpoint | Description |
|------------------|----------|-------------|
| Auth Service     | `POST /auth/login` | User login — returns JWT token |
| Auth Service     | `POST /auth/register` | User registration |
| User Service     | `GET /users/{id}` | Get user info (requires auth) |
| Order Service    | `POST /orders` | Create new order (requires auth) |
| Order Service    | `GET /orders/{orderId}` | Fetch order details |  
| ...              | ...      | ... |



## 🧩 Architecture Diagram

                                  ┌────────────────────────────┐
                                  │     Git Config Repository   │
                                  │ (delivery-dev.yml etc...)  │
                                  └─────────────▲──────────────┘
                                                │
                                  ┌─────────────┴──────────────┐
                                  │   Spring Cloud Config Server │
                                  │ central config for all apps  │
                                  └─────────────▲──────────────┘
                                                │ Config Fetch
           ┌────────────────────────────────────┼────────────────────────────────────────┐
           │                                    │                                         │
           ▼                                    ▼                                         ▼
┌───────────────────────┐           ┌───────────────────────┐             ┌────────────────────────┐
│ USER-SERVICE           │           │ ORDER-SERVICE          │             │ EMAIL-SERVICE          │
│ @FeignClient           │           │ @FeignClient           │             │ @FeignClient           │
│ Fault Tolerance Layer  │           │ Fault Tolerance Layer  │             │ Fault Tolerance Layer  │
│ - Circuit Breaker      │           │ - Retry                │             │ - BulkHead             │
│ - Retry                │           │ - Circuit Breaker      │             │ - Timeout              │
│ - Rate Limiter         │           │ - BulkHead             │             │ - Retry                │
│ - Bulkhead             │           │ - TimeLimiter          │             │                        │
│ - Timeout              │           │                        │             │                        │
└────────────▲────────────┘           └────────────▲────────────┘             └────────────▲──────────┘
             │                                      │                                        │
             │      Service-to-Service Calls via    │                                        │
             │      Feign + Resilience4j            │                                        │
             │──────────────────────────────────────▶────────────────────────────────────────▶│
             │                                                                              │
             │                                                                              │
             ▼                                                                              ▼
┌────────────────────────┐                                                  ┌────────────────────────┐
│ DELIVERY-SERVICE        │                                                  │  RESILIENCE4j LAYERS   │
│ @FeignClient            │                                                  │  (Shared for All)      │
│ Fault Tolerance:        │                                                  │  - Circuit Breaker     │
│  - Circuit Breaker      │                                                  │  - Retry               │
│  - Bulkhead             │                                                  │  - TimeLimiter         │
│  - Retry                │                                                  │  - RateLimiter         │
│  - Timeout              │                                                  │  - BulkHead            │
└────────────▲────────────┘                                                  └────────────▲──────────┘
             │                                                                               │
             └───────────────────────────────Service Discovery────────────────────────────────┘
                                               (via Eureka)

                           ┌──────────────────────────────────────────┐
                           │              Eureka Server               │
                           │   Service Registry (All Services)        │
                           └──────────────────▲───────────────────────┘
                                              │
                                              │
                                     Load Balanced by
                                        Gateway
                                              │
                           ┌──────────────────┴──────────────────┐
                           │      SPRING CLOUD GATEWAY           │
                           │  - Routes via lb://SERVICE-ID       │
                           │  - Load Balanced Requests           │
                           │  - Entry point for all clients      │
                           └──────────────────▲──────────────────┘
                                              │
                                          External Traffic
                                              │
                                    ┌─────────┴─────────┐
                                    │   Angular / Mobile │
                                    │   Postman / Web    │
                                    └────────────────────┘


## 🧰 Tech Stack & Tools

- Java / Spring Boot  
- Spring Cloud (Eureka, Gateway, Config Server, LoadBalancer)  
- Spring Security + JWT  
- OpenFeign for inter-service calls  
- Resilience4j for fault tolerance  
- Maven / Gradle  
- Git for version control  

---

## ✅ What This Project Demonstrates / My Learning Outcomes

- Real-world microservices architecture design  
- Secure authentication and authorization across distributed services  
- Dynamic service discovery & configuration management  
- Load balancing, fault tolerance & resilience for production-readiness  
- Modular code organization & separation of concerns  
- Inter-service communication using Feign + resilience patterns  
- Environment-independent configuration management  

---

## 📈 Potential Improvements / Future Work

- Add integration tests / unit tests  
- Add API documentation (Swagger / OpenAPI)  
- Containerization (Docker) + Deployment scripts / Kubernetes setup  
- Logging & metrics / monitoring integration (Prometheus, Grafana)  
- Add more microservices (payment, product catalog, inventory) to expand functionality  
- Add sample data / sample UI (frontend) to make it a full-stack demo  

---

## 🙋‍♂️ About Me

I’m a Full stack engineer passionate about building scalable, cloud-native, and resilient systems — constantly learning and implementing real-world architecture patterns using Spring Boot and Spring Cloud.  
If you like what you see, feel free to connect or reach out!

---

