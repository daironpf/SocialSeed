# 🌱 Social Seed — Build Your Own Social Network with Spring Boot, Neo4j & Microservices

## 🚀 Overview

**Social Seed** provides a complete backend ecosystem for building a social network powered by microservices and graph relationships.
It is designed to be **extendable**, **distributed**, and **ready for integration** with mobile or web clients.

A dedicated **Android application** will be published on the **Google Play Store** to interact directly with this backend, allowing users to experience the platform in real-time.

---

## 📊 Project Status

![GitHub closed issues](https://img.shields.io/github/issues-closed/daironpf/SocialSeed?include_prs=false)
![Open Issues](https://img.shields.io/github/issues/daironpf/SocialSeed?include_prs=false)
![Last Commit](https://img.shields.io/github/last-commit/daironpf/SocialSeed)
![Repo Size](https://img.shields.io/github/repo-size/daironpf/SocialSeed)

## ⚙️ Technologies

![Java](https://img.shields.io/badge/Java-17+-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![API](https://img.shields.io/badge/API-REST-orange)  <!-- Public API -->

![Neo4j](https://img.shields.io/badge/Database-Neo4j-blue)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)

![Architecture](https://img.shields.io/badge/Architecture-Microservices-orange)
![Kafka](https://img.shields.io/badge/Messaging-Apache%20Kafka-black)
![gRPC](https://img.shields.io/badge/Communication-gRPC-brightgreen)  <!-- Internal microservice communication -->

![Docker](https://img.shields.io/badge/Container-Docker-blue)
![Docker](https://img.shields.io/badge/Deployment-Docker%20Compose-blue)



## 📄 License

[![License: Apache-2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

> **Social Seed** is a modular and scalable social network backend built with **Spring Boot**, **Neo4j**, and **microservices architecture**.
> It serves as a foundation for developers to create and extend social platforms with modern, flexible backend components.

---

## 🧩 Key Features

* **🧠 Modular Microservices Architecture** — Each service handles a specific domain for clean scalability.
* **🔗 Graph Database** — Built on **Neo4j**, enabling advanced relationship modeling (friends, followers, likes).
* **💬 Core Social Interactions** — User profiles, follow system, posts, comments, likes, and private messaging.
* **⚙️ API-First Approach** — Fully REST-based backend designed for mobile and web clients.
* **📈 Scalable & Extensible** — Easily deployable via Docker and adaptable to new use cases.
* **🆓 Open Source** — Licensed under **Apache 2.0**, allowing free use, modification, and distribution.

---

## 🧠 Graph Data Model

The project uses a graph-oriented data structure to represent relationships between users, posts, and interactions:

![Graph Data Model](https://github.com/daironpf/SocialSeed/blob/main/Neo4j/Graph%20Data%20Model/SocialSeed%20\(SS\).png)

---

## 🏗️ Architecture Overview

Social Seed is built on a **microservices-based backend**, where each component operates independently and communicates via REST or messaging events:

* **Auth Service** — User registration, Authentication & authorization using JWT.
* **SocialUser Service** — User profiles, and account data.
* **Post Service** — Post creation and retrieval.
* **Comment Service** — Comments associated with posts.
* **Reaction Service** — Likes and reactions to posts.
* **Relationship Service** — Following and friendships between users.
* **Messaging Service** — Private messaging between users.
* **Notification Service** — Real-time and event-based notifications.
* **Media Service** — Image and file upload management.

---

## 🛠️ Getting Started

### Prerequisites

* **Java 17+**
* **Docker & Docker Compose**
* **Neo4j Database**
* **Maven 3.8+**

### Installation

1. **Clone the repository:**

   ```bash
   git clone git@github.com:daironpf/SocialSeed.git
   cd SocialSeed
   ```

2. **Start all services using Docker Compose:**

   ```bash
   docker-compose up --build
   ```

3. **Access the backend APIs:**

   ```
   http://localhost:8085
   ```

---

## 🎨 Android App Integration

An official **Android application** will be published on the **Google Play Store**, providing a seamless interface to interact with the Social Seed backend — allowing users to:

* Create and manage profiles
* Follow and interact with friends
* Share posts and comments
* Send messages in real time

The app will serve as the **default client** for this backend, but the API is fully open for custom frontends.

<!--
---

## 🤝 Contributing

Contributions are welcome!
If you’d like to contribute to Social Seed:

1. Fork the repository
2. Create a feature branch (`feature/new-feature`)
3. Commit your changes
4. Open a Pull Request

Please follow conventional commit and PR naming practices.
-->
---

## 📬 Contact

**Developer:** Dairon Pérez Frías
📧 **Email:** [dairon.perezfrias@gmail.com](mailto:dairon.perezfrias@gmail.com)

---

<!--

## 🙏 Acknowledgments

Special thanks to **JetBrains** for supporting open-source development with free licenses, which make this project’s development faster and smoother.

![JetBrains logo](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)

---
-->

## 📜 License

This project is licensed under the **Apache License 2.0**.
See the [LICENSE](LICENSE) file for full details.

---
