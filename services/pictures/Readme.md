Run locally:

docker run -p 8080:8080 -e TARGET_DIR=/mnt/data -v /folder:/mnt/data IMAGE


Build image:
docker build -t docker.redby.fr/gallery/service-pictures:latest .

Push image:
docker push docker.redby.fr/gallery/service-pictures:latest



