# Build stage
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=builder /app/target/thumbnails-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
