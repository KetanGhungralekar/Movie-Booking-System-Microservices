

---


# 🎬 MovieTime – Backend Microservices Platform  
**A Cloud-Native, Production-Ready Distributed System**

![Microservices](https://img.shields.io/badge/Architecture-Microservices-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Orchestrated-blueviolet)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-red)
![ELK](https://img.shields.io/badge/Logging-ELK%20Stack-orange)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-informational)
![RabbitMQ](https://img.shields.io/badge/Messaging-RabbitMQ-yellow)

---

## Overview

MovieTime Backend is a fully containerized, microservices-based backend platform for an online movie ticket booking system. It is built using Spring Boot, orchestrated with Kubernetes, automated through Jenkins CI/CD, and monitored using the ELK Stack.

This repository includes:

- All backend microservices  
- Docker and Docker Compose configurations  
- Kubernetes manifests  
- Jenkins CI/CD pipelines  
- RabbitMQ and Redis setup  
- PostgreSQL databases  
- Centralized logging with ELK  

🚫 Frontend is intentionally excluded.

---

## 📚 Table of Contents

- 🎯 Project Goals  
- 🧱 System Architecture  
- 🧩 Microservices Overview  
- ⚙️ Technology Stack  
- 🗂 Repository Structure  
- 🐳 Local Setup with Docker  
- ☸️ Kubernetes Deployment  
- 🔄 CI/CD with Jenkins  
- 📨 Messaging with RabbitMQ  
- ⚡ Redis Caching  
- 🗄 Databases  
- 📊 Centralized Logging (ELK)  
- 🌐 API Gateway & Eureka  
- 🔐 Security  
- 🧪 Testing Strategy  
- 🔍 Monitoring & Observability  
- 🚀 Deployment Workflow  
- 🧭 Future Enhancements  

---

## 🎯 Project Goals

- Build a **production-ready backend system**
- Apply **microservices architecture**
- Enable **event-driven communication**
- Implement **CI/CD automation**
- Achieve **centralized logging and monitoring**
- Ensure **scalability, reliability, and fault tolerance**

---

## 🧱 System Architecture

The backend follows a **cloud-native microservices architecture** with the following layers:

- **Client Layer** – handled externally by frontend  
- **API Gateway Layer** – centralized request routing  
- **Service Discovery Layer** – dynamic service registry  
- **Business Services Layer** – core microservices  
- **Data Layer** – independent PostgreSQL databases  
- **Messaging Layer** – RabbitMQ  
- **Caching Layer** – Redis  
- **Observability Layer** – ELK Stack  
- **DevOps Layer** – Docker, Kubernetes, Jenkins  

All services communicate using:
- ✅ **REST APIs** (synchronous)
- ✅ **RabbitMQ events** (asynchronous)

---

## 🧩 Microservices Overview

| Service | Responsibility |
|--------|----------------|
| 🔀 API Gateway | Single entry point |
| 🧭 Discovery Server | Service registry |
| 👤 User Service | Authentication & users |
| 🎬 Movie Service | Movies & shows |
| 🎫 Booking Service | Ticket booking |
| 💳 Payment Service | Payment processing |
| 📧 Notification Service | Email alerts |

---

## ⚙️ Technology Stack

| Category | Technology |
|---------|------------|
| Language | Java 17 |
| Framework | Spring Boot |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Database | PostgreSQL |
| Messaging | RabbitMQ |
| Cache | Redis |
| Auth | JWT + Spring Security |
| Containers | Docker |
| Orchestration | Kubernetes |
| CI/CD | Jenkins |
| Logging | Logback, Filebeat |
| Log Processing | Logstash |
| Storage | Elasticsearch |
| Visualization | Kibana |

---

## 🗂 Repository Structure



movie-time-backend/
│
├── api-gateway/
├── discovery-server/
├── user-service/
├── movie-service/
├── booking-service/
├── payment-service/
├── notification-service/
│
├── docker-compose.yml
│
├── k8s/
│   ├── api-gateway/
│   ├── discovery-server/
│   ├── user-service/
│   ├── movie-service/
│   ├── booking-service/
│   ├── payment-service/
│   ├── notification-service/
│   ├── databases/
│   ├── rabbitmq/
│   ├── redis/
│   ├── ingress/
│
├── elk/
│   ├── filebeat.yml
│   ├── logstash.conf
│   └── k8s/
│
├── jenkins/
│   └── Jenkinsfiles/
│
└── README.md



---

## 🐳 Local Setup with Docker

### ✅ Prerequisites
- Docker
- Docker Compose

### ▶️ Start Entire Backend Stack
```bash
docker compose up -d
````

### 🌍 Access URLs

| Component            | URL                                              |
| -------------------- | ------------------------------------------------ |
| Eureka               | [http://localhost:8761](http://localhost:8761)   |
| API Gateway          | [http://localhost:8085](http://localhost:8085)   |
| Movie Service        | [http://localhost:8086](http://localhost:8086)   |
| Booking Service      | [http://localhost:8087](http://localhost:8087)   |
| Notification Service | [http://localhost:8088](http://localhost:8088)   |
| Payment Service      | [http://localhost:8089](http://localhost:8089)   |
| RabbitMQ UI          | [http://localhost:15672](http://localhost:15672) |
| Kibana               | [http://localhost:5601](http://localhost:5601)   |

---

## ☸️ Kubernetes Deployment

### 🏷 Create Namespaces

```bash
kubectl create namespace movie-app
kubectl create namespace elk
```

### 🧱 Deploy Infrastructure

```bash
kubectl apply -f k8s/discovery-server/
kubectl apply -f k8s/rabbitmq/
kubectl apply -f k8s/redis/
kubectl apply -f k8s/databases/
```

### 🚀 Deploy Microservices

```bash
kubectl apply -f k8s/api-gateway/
kubectl apply -f k8s/user-service/
kubectl apply -f k8s/movie-service/
kubectl apply -f k8s/booking-service/
kubectl apply -f k8s/payment-service/
kubectl apply -f k8s/notification-service/
```

### 🌍 Deploy Ingress

```bash
kubectl apply -f k8s/ingress/
```

---

## 🔄 CI/CD with Jenkins

Each microservice has its **own Jenkins pipeline**:

### ✅ Pipeline Stages

1. Source Code Checkout
2. Change Detection
3. Maven Build & Unit Tests
4. Docker Image Build
5. Docker Push
6. Kubernetes Rolling Update
7. Deployment Verification

### 🔁 CI/CD Flow

```
Git Push → Jenkins → Build → Test → Docker → Kubernetes → Live Production
```

---

## 📨 Messaging with RabbitMQ

RabbitMQ enables **event-driven communication**:

| Producer        | Event             | Consumer             |
| --------------- | ----------------- | -------------------- |
| Booking Service | Booking Confirmed | Notification Service |
| Payment Service | Payment Success   | Booking Service      |

✅ Loose coupling
✅ Asynchronous processing
✅ Reliable delivery

---

## ⚡ Redis Caching

Redis is used for:

* Fast access to booking state
* Reduced database load
* Improved response time
* High-performance in-memory operations

---

## 🗄 Databases

Each service owns its **dedicated PostgreSQL database**:

| Service         | Database   |
| --------------- | ---------- |
| User Service    | user_db    |
| Movie Service   | movie_db   |
| Booking Service | booking_db |

Kubernetes supports:

* Native PostgreSQL StatefulSets
* Zalando PostgreSQL Operator

---

## 📊 Centralized Logging with ELK

### Components

* 🐳 **Filebeat** – collects container logs
* 🧰 **Logstash** – parses and forwards logs
* 🗃 **Elasticsearch** – stores and indexes logs
* 📈 **Kibana** – visualizes logs

### Log Index Format

```
movie-logs-YYYY.MM.dd
```

✅ Cross-service tracing
✅ Real-time debugging
✅ Production observability

---

## 🌐 API Gateway & Eureka

* **Eureka** dynamically registers all services
* **API Gateway** routes requests
* Enables **zero-downtime scaling**
* Supports **dynamic service discovery**

---

## 🔐 Security

* JWT-based authentication
* Encrypted passwords using BCrypt
* Role-based access control
* Secure inter-service communication
* Kubernetes internal networking

---

## 🧪 Testing Strategy

Each service supports:

* Spring Boot context tests
* Profile-based testing
* Mocked RabbitMQ & SMTP
* H2 in-memory database
* Isolated microservice testing

---

## 🔍 Monitoring & Observability

* Real-time logs via Kibana
* Service registry view via Eureka
* Pod-level monitoring via Kubernetes
* Jenkins pipeline monitoring

---

## 🚀 Deployment Workflow

1. Developer pushes code
2. Jenkins pipeline triggers automatically
3. Maven build and tests run
4. Docker image is built
5. Image is pushed to Docker Hub
6. Kubernetes deployment is updated
7. Rolling update ensures zero downtime
8. Logs visible instantly in Kibana

---

## 🧭 Future Enhancements

* Horizontal Pod Autoscaling (HPA)
* Prometheus & Grafana monitoring
* Distributed tracing with Jaeger
* OAuth2 authentication
* API rate limiting
* Terraform Infrastructure as Code
* GitOps with ArgoCD

---

## ✅ Conclusion

This backend platform represents a **complete enterprise-grade microservices ecosystem** with:

✅ Scalability
✅ High availability
✅ CI/CD automation
✅ Centralized logging
✅ Secure authentication
✅ Cloud-native deployment

It is suitable for:

* Academic final-year projects
* Enterprise-grade PoC systems
* Cloud and DevOps research

---

⭐ If this project was helpful, consider starring the repository!

