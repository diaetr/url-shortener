# ---------- build stage ----------
FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /home/gradle/project

# copy gradle metadata and wrapper first (better layer caching)
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY gradlew ./

# normalize Windows line endings for gradlew, then make it executable
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# copy source code
COPY src src

# build a fat jar (skip tests here for faster docker builds)
RUN ./gradlew clean bootJar -x test

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy the jar produced in the builder stage
ARG JAR_FILE=/home/gradle/project/build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]