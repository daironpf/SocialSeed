# 🌱 Social Seed — Build Your Own Social Network with Spring Boot, Neo4j & Microservices

## 🚀 Overview

**Social Seed** is a professional-grade backend ecosystem designed to build scalable, decentralized, and ethical social networks. Powered by a **microservices architecture** and **graph-based relationships**, it provides a robust foundation for modern social platforms.

A dedicated **Android application** is part of the ecosystem, ensuring a seamless, real-time experience for end-users, while the backend remains fully extensible for web and third-party clients.

---

## 📊 Project Status

![GitHub closed issues](https://img.shields.io/github/issues-closed/daironpf/SocialSeed?include_prs=false)
![Open Issues](https://img.shields.io/github/issues/daironpf/SocialSeed?include_prs=false)
![Last Commit](https://img.shields.io/github/last-commit/daironpf/SocialSeed)
![Repo Size](https://img.shields.io/github/repo-size/daironpf/SocialSeed)

## ⚙️ Technologies

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Neo4j](https://img.shields.io/badge/Database-Neo4j-blue)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![Kafka](https://img.shields.io/badge/Messaging-Apache%20Kafka-black)
![gRPC](https://img.shields.io/badge/Communication-gRPC-brightgreen)
![Docker](https://img.shields.io/badge/Container-Docker-blue)

---

### 🏛️ The Governance Handshake: Brain & Arm
The most innovative part of the architecture is the decoupling of decision and execution:
* **Governance Service (The Brain):** An AI-sentinel using **Neo4j Graph Data Science** and **LLMs (Gemini)** to analyze relationships and intent (Bullying detection, toxicity, data theft ...).
* **Nexus Service (The Arm):** A dedicated orchestrator that executes administrative commands (Force Logouts, Bans, Role updates ...) across the mesh via **gRPC**, ensuring a 100% auditable and transparent system.


---

## 🧠 Graph Data Model

The project leverages a graph-oriented structure to represent the complexity of human connections:

![Graph Data Model](https://github.com/daironpf/SocialSeed/blob/main/Neo4j/Graph%20Data%20Model/SocialSeed%20\(SS\).png)

---
## 🏗️ Technical Architecture: The Platform Core

Social Seed is built on a custom **Platform Layer** that ensures consistency across the entire mesh:

### 🧩 SocialSeed Platform Starters
To maintain a "Don't Repeat Yourself" (DRY) philosophy, we developed custom Spring Boot Starters located in `/platform`:

- **`socialseed-api-response-starter`**: Standardizes the REST API response structure across all microservices, providing a unified wrapper for all payloads.
- **`socialseed-contracts`**: Centralized repository for Protobuf definitions and generated classes used in gRPC and Kafka communication.
- **`socialseed-error-handling-starter`**: Implements a global exception handling strategy to transform exceptions into standardized, secure error responses.
- **`socialseed-validation-starter`**: Provides custom Jakarta Bean Validation annotations and reusable logic to enforce data integrity consistently.






## 🏗️ Architecture Overview

Social Seed is a distributed system where services communicate via REST, gRPC, or event-driven messaging (Kafka):

* **Auth Service** — Secure identity management and JWT authorization.
* **SocialUser Service** — Profile and account metadata management.
* **Post/Comment/Reaction Services** — Core interaction layers.
* **Relationship Service (Neo4j)** — High-speed social graph management.
* **Governance Service (AI-Core)** — Real-time moderation, sentiment analysis, and interaction oversight.
* **Nexus Service (The Arm)** — A dedicated orchestrator that executes administrative commands.
* **Messaging & Notification Services** — Real-time communication and event-driven alerts.

---

## 🗺️ Roadmap & Strategic Collaboration

Social Seed is evolving to set a new standard in ethical social networking. We are currently focusing on:

* **Cognitive Governance (LLM Integration):** Architecting seamless integration with **Google Gemini** for deep semantic understanding and real-time user assistance.
* **Graph Data Science (GDS):** Implementing **Neo4j GDS** algorithms to detect complex social patterns, prevent harassment, and measure community health.
* **Enterprise & Educational Pilots:** Optimizing the core for deployment in schools and corporate intranets where safe, supervised social interaction is critical.
* **Total Sovereignty:** Providing a "Network-in-a-box" solution deployable via Docker for private institutions.

---

## 🧠 Key Features

* **🔗 Graph-First Social Logic** — **Neo4j** powers friends, followers, and complex behavioral clusters.
* **🛡️ Hybrid Governance** — AI suggests, Humans decide. A "Human-in-the-Loop" system for ethical moderation.
* **⚡ Ultra-fast Execution** — Internal communication via **gRPC** and real-time event propagation via **Apache Kafka**.
* **🏢 Multi-Sector Adaptability** — Tailored configurations for schools (bullying prevention) or enterprises (IP protection) and more.

---

## 🧩 Microservices Roadmap

| Service | Status | Tech Stack | Responsibility |
| :--- | :--- | :--- | :--- |
| **Auth** | ✅ Active | PostgreSQL / Redis | Identity, JWT, RBAC |
| **Nexus** | 🚧 Beta | gRPC / Kafka | Admin Orchestration & Enforcement |
| **Governance**| 🧠 Designing| Neo4j / Gemini | AI Behavior Analysis & Mentorship |
| **SocialUser**| ✅ Active | Neo4j | Profile & Metadata Management |
| **Api-Gateway**| 🛰️ Active | Spring Cloud | Entry point & Route Security |



---


## 🛠️ Getting Started

1. **Clone the repository:**
```bash
git clone git@github.com:daironpf/SocialSeed.git
cd SocialSeed

```

2. **Start the ecosystem using Docker Compose:**
```bash
docker-compose up --build

```

3. **API Access:**
```
http://localhost:8085

```


---

## 🎨 Android App Integration

An official **Android application** will be published on the **Google Play Store**, serving as the flagship client for the Social Seed backend. It provides a seamless interface for profile management, real-time messaging, and social interaction.

---

## 📜 License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](https://www.google.com/search?q=LICENSE) file for full details.

---

**Developer:** Dairon Pérez Frías

📧 **Email:** [dairon.perezfrias@gmail.com](mailto:dairon.perezfrias@gmail.com)
