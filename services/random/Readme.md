Run locally:

docker run -p 8000:8000 \
  -e YEARS_SERVICE_URL=http://service-years:8000 \
  -e ALBUMS_SERVICE_URL=http://service-albums:8000 \
  IMAGE


Build image:
docker build -t docker.redby.fr/gallery/service-random:latest .

Push image:
docker push docker.redby.fr/gallery/service-random:latest

GET /random

Choisit aléatoirement une année, un album puis 3 images de cet album et
renvoie l'ensemble en JSON. Le token OIDC (header Authorization: Bearer ...)
est vérifié puis retransmis aux services years et albums.

Réponse :
{
  "year": 2023,
  "album": { "date": "2023-07-14", "name": "...", "id": "...", "thumbnail": "..." },
  "images": ["IMG_0001.jpg", "sub/IMG_0042.jpg", "IMG_0099.jpg"]
}
