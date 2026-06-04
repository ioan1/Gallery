# Build stage
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Install ImageMagick
RUN apt-get update \
	&& apt-get install -y --no-install-recommends imagemagick \
	&& rm -rf /var/lib/apt/lists/*

RUN apt-get update \
	&& apt-get install -y --no-install-recommends imagemagick libheif1 libde265-0

COPY --from=builder /app/target/thumbnails-1.0.0.jar app.jar

# Mount point for photos
VOLUME /photos

EXPOSE 8080
ENV ALBUMS_SERVICE_URL=http://service-albums:8000
ENTRYPOINT ["java", "-jar", "app.jar"]
