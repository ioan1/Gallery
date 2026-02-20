FROM arm32v7/openjdk:17-jdk-slim AS build

# Installer Maven manuellement
ENV MAVEN_VERSION=3.8.8
ENV MAVEN_HOME=/opt/maven
RUN apt-get update && apt-get install -y wget tar && \
	wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
	tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
	mv /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
	ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn && \
	rm apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
	apt-get clean && rm -rf /var/lib/apt/lists/*

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
