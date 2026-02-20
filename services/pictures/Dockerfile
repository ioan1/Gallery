FROM maven:3.8.8-openjdk-17-slim AS build

WORKDIR /build

# Copier le code source et pom.xml
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM arm32v7/openjdk:17-jdk-slim AS runtime

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /build/target/pictures-service-0.0.1-SNAPSHOT.jar .

# Exposer le port (adapter si besoin)
EXPOSE 8080

# Commande pour lancer le JAR
CMD ["java", "-jar", "pictures-service-0.0.1-SNAPSHOT.jar"]

# ...fin du Dockerfile multi-stage Java/Maven...
