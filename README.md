# URL Shortener Service

A simple **URL Shortener REST API** implemented in **Java** and **Spring Boot**, built with **Gradle**, containerized with **Docker**, and deployable on **Kubernetes** using **Helm**.

---
## Features

- Shorten URLs via REST API (`POST /shorten`)
- Redirect to the original URL via shortened ID (`GET /{id}`)
- In-memory thread-safe store (`ConcurrentHashMap`)
- Basic URL validation (`http`/`https`, syntactic check)
- Unit tests for each endpoint
- Dockerized for easy container deployment
- Helm chart for Kubernetes deployment
- CI pipeline via GitHub Actions

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot
- **Build:** Gradle (wrapper included)
- **Tests:** JUnit 5, Spring MockMvc
- **Container** Docker 
- **Deployment** Kubernetes + Helm
- **CI Pipeline** GitHub Actions
- **Storage** In-memory Map

