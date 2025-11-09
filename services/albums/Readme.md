Run locally:

docker run -p 8000:8000 -e TARGET_DIR=/mnt/data -v /folder:/mnt/data IMAGE


Build image:
docker build -t docker.redby.fr/gallery/service-albums:latest .

Push image:
docker push docker.redby.fr/gallery/service-albums:latest

GET /albums/{year}


