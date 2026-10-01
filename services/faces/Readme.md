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

Le service reconstruit ensuite l’URL de la miniature :

```text
https://gallery.redby.fr/thumbnails/small/2008/d467cc0f?name=IMAGE_517.jpg
```

## Endpoints

- GET /health
- POST /faces/index

## Variables d’environnement

- `THUMBNAILS_BASE_URL` : base URL du service thumbnails, défaut `https://gallery.redby.fr`
- `ALLOWED_HOST` : hôte autorisé, défaut `gallery.redby.fr`
- `DATABASE_URL` : chaîne PostgreSQL, défaut `postgresql://faces:faces@service-postgres:5432/faces`

## Docker

```bash
docker build -t gallery/faces .
```

## Déploiement Kubernetes

Les manifests sont dans `infra/services/faces` et `infra/services/postgres`.
