FROM store/oracle/serverjre:8
COPY ./target/service-pictures-*.jar /app/service-pictures.jar
RUN ls -l /app/
EXPOSE 8085/tcp
ENTRYPOINT java -DGALLERY_PATH=/gallery -jar /app/service-pictures.jar --server.port=8085
