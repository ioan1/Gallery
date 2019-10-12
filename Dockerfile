FROM store/oracle/serverjre:8
COPY ./target/service-categories-*.jar /app/service-categories.jar
RUN ls -l /app/
EXPOSE 8083/tcp
ENTRYPOINT java -DGALLERY_PATH=/gallery -jar /app/service-categories.jar --server.port=8083
