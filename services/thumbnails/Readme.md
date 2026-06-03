# Thumbnails Service

Service microservice Spring Boot pour la gestion des thumbnails, compatible Java 25.

## Prérequis

- Java 25+
- Maven 3.9+
- Docker

## Configuration

Le service écoute sur le port **8080**.

## Build et Exécution

### En local avec Maven

```bash
mvn clean package
java -jar target/thumbnails-1.0.0.jar
```

### Avec Docker

```bash
docker build -t thumbnails-service .
docker run -p 8080:8080 thumbnails-service
```

## Endpoints

- `GET /` - Informations du service
- `GET /health` - Vérification de l'état du service
- `GET /actuator/health` - Health check détaillé

## Dépendances principales

- Spring Boot 3.4.0
- Java 25
- Spring Boot Web
- Spring Boot Actuator
