# Faces service

Service dédié à la détection de visages humains et au stockage des embeddings dans PostgreSQL avec pgvector.

## Input

```json
{
  "category": "2008",
  "album": "d467cc0f",
  "name": "IMAGE_517.jpg"
}
```

Le service reconstruit ensuite l’URL de l’image originale et transmet le JWT à thumbnails :

```text
https://gallery.redby.fr/thumbnails/original/2008/d467cc0f?name=IMAGE_517.jpg
```

## Endpoints

- GET /health
- POST /faces/index
- GET /faces/{year}/{albumId}/people-count

Le compteur regroupe les embeddings de l’album par similarité cosinus. Il est recalculé à la demande.

## Variables d’environnement

- `THUMBNAILS_BASE_URL` : base URL du service thumbnails, défaut `https://gallery.redby.fr`
- `ALLOWED_HOST` : hôte autorisé, défaut `gallery.redby.fr`
- `DATABASE_URL` : chaîne PostgreSQL, défaut `postgresql://faces:faces@service-postgres:5432/faces`
- `MAX_IMAGE_BYTES` : taille maximale de l’image source, défaut `52428800` (50 MiB)
- `FACE_DET_SIZE` : taille de détection InsightFace, défaut `1024`
- `FACE_DET_THRESHOLD` : seuil de détection, défaut `0.35` (diminuer pour augmenter la sensibilité)
- `PERSON_SIMILARITY_THRESHOLD` : similarité cosinus minimale pour regrouper deux visages, défaut `0.45`

## Docker

```bash
docker build -t gallery/faces .
```

## Déploiement Kubernetes

Les manifests sont dans `infra/services/faces` et `infra/services/postgres`.
