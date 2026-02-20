FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /build

# Copier le code source et pom.xml
COPY pom.xml .
COPY src ./src

# Builder le JAR
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk AS runtime

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /build/target/pictures-service-0.0.1-SNAPSHOT.jar .

# Exposer le port (adapter si besoin)
EXPOSE 8080

# Commande pour lancer le JAR
CMD ["java", "-jar", "pictures-service-0.0.1-SNAPSHOT.jar"]

# ...fin du Dockerfile multi-stage Java/Maven...
