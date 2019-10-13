FROM store/oracle/serverjre:8
COPY ./target/service-albums-*.jar /app/service-albums.jar
RUN ls -l /app/
EXPOSE 8084/tcp
ENTRYPOINT java -DGALLERY_PATH=/gallery -jar /app/service-albums.jar --server.port=8084
