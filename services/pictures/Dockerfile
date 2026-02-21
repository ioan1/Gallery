FROM arm32v7/eclipse-temurin:17-jdk AS build

WORKDIR /build

# Copier le code source et pom.xml
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM arm32v7/eclipse-temurin:17-jdk AS runtime

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /build/target/pictures-service-0.0.1-SNAPSHOT.jar .

# Exposer le port (adapter si besoin)
EXPOSE 8080

# Commande pour lancer le JAR
CMD ["java", "-jar", "pictures-service-0.0.1-SNAPSHOT.jar"]

# ...fin du Dockerfile multi-stage Java/Maven...
