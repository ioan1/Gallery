FROM store/oracle/serverjre:8
COPY ./target/service-statistics-*.jar /app/service-statistics.jar
RUN ls -l /app/
EXPOSE 8086/tcp
ENTRYPOINT java -DGALLERY_PATH=/gallery -jar /app/service-statistics.jar --server.port=8086
