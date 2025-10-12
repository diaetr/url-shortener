# URL Shortener (Spring Boot)

A tiny URL shortener built with **Java 17** and **Spring Boot**.  
It exposes two endpoints:

- `POST /shorten` — accepts a long URL and returns a short identifier.
- `GET /{id}` — redirects to the original URL if the id exists, otherwise `404`.

---

## Features

- Java 17 + Spring Boot (Gradle)
- Two endpoints (`POST /shorten`, `GET /{id}`)
- In-memory thread-safe store (`ConcurrentHashMap`)
- Basic URL validation (`http`/`https`, syntactic check)
- Tests (at least one per endpoint)
- Conventional commits recommended

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot
- **Build:** Gradle (wrapper included)
- **Tests:** JUnit 5, Spring MockMvc

---

## Quick Start

```bash
# run in dev mode
./gradlew bootRun

# or build a runnable jar
./gradlew clean build
java -jar build/libs/*-SNAPSHOT.jar
