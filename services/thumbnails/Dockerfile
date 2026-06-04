# Build stage
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jdk
WORKDIR /app

# Install ImageMagick, HEIC support and ffmpeg
RUN apt-get update \
     && apt-get install -y --no-install-recommends \
         imagemagick \
         libheif1 \
         libde265-0 \
         ffmpeg \
     && rm -rf /var/lib/apt/lists/*

# Ensure native HEIF libraries are discoverable by Java (some distros put them in /usr/lib/x86_64-linux-gnu)
RUN ldconfig || true \
        && for f in /usr/lib/x86_64-linux-gnu/libheif.so* /usr/lib/x86_64-linux-gnu/libde265.so*; do \
                 if [ -e "$f" ]; then ln -sf "$f" /usr/lib/$(basename "$f"); fi; \
             done || true

ENV LD_LIBRARY_PATH=/usr/lib:/usr/lib/x86_64-linux-gnu

COPY --from=builder /app/target/thumbnails-1.0.0.jar app.jar

# Increase ImageMagick policy limits so large HEIC images are accepted
# Some distributions place the policy at /etc/ImageMagick/policy.xml or /etc/ImageMagick-6/policy.xml
RUN mkdir -p /etc/ImageMagick \
        && cat > /etc/ImageMagick/policy.xml <<'POLICY'\
<?xml version="1.0" encoding="UTF-8"?>\
<policies>\
    <!-- Increase maximum dimensions and area to allow large images -->\
    <policy domain="resource" name="width" value="100000"/>\
    <policy domain="resource" name="height" value="100000"/>\
    <policy domain="resource" name="area" value="100000000"/>\
    <!-- Increase memory/map limits to reasonable amounts for conversion -->\
    <policy domain="resource" name="memory" value="2GiB"/>\
    <policy domain="resource" name="map" value="4GiB"/>\
</policies>\
POLICY\
        && mkdir -p /etc/ImageMagick-6 || true \
        && ln -sf /etc/ImageMagick/policy.xml /etc/ImageMagick-6/policy.xml || true

# Mount point for photos
VOLUME /photos

EXPOSE 8080
ENV ALBUMS_SERVICE_URL=http://service-albums:8000
ENTRYPOINT ["java", "-jar", "app.jar"]
