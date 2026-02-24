FROM arm32v7/eclipse-temurin:17

#WORKDIR /app

# Configuration JVM pour containers
#ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xms128m -Xmx256m -XX:+UseContainerSupport"

# Si le JAR est déjà buildé par le workflow, décommentez la ligne suivante et commentez celle du dessus :
#COPY target/pictures-service-0.0.1-SNAPSHOT.jar .

# Exposer le port (adapter si besoin)
#EXPOSE 8080

# Commande pour lancer le JAR
CMD ["java", "-version"]

# ...fin du Dockerfile multi-stage Java/Maven...
