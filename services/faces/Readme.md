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
- GET /faces/people : groupes les visages indexés de tous les albums
- PUT /faces/people/label : associe un libellé à un groupe de visages

Le corps de la requête de libellé contient les identifiants de visages renvoyés par
`GET /faces/people` et le texte à enregistrer :

```json
{
  "face_ids": [12, 18],
  "label": "Camille"
}
```

Les groupes globaux sont calculés par similarité cosinus. Depuis le frontend, ouvrez
« Identifier les visages » pour consulter ces groupes et enregistrer un nom ou un
libellé. L’association est conservée dans PostgreSQL et les nouveaux visages similaires
retrouvent ce libellé lors des scans suivants.

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

### Persistance PostgreSQL

Le manifeste `infra/services/postgres/pvc.yaml` crée un volume persistant de 10 Gio. PostgreSQL reste sur un seul pod (`replicas: 1`). Comme la base actuelle peut être abandonnée, les commandes suivantes la recréent vide sur le PVC :

```bash
kubectl apply -f infra/services/postgres/pvc.yaml
kubectl apply -f infra/services/postgres/deployment.yaml
kubectl -n gallery rollout status deployment/service-faces-postgres
kubectl apply -f infra/services/faces/deployment.yaml
```

### pgAdmin

pgAdmin est disponible sur `https://gallery.redby.fr/pgadmin/`. Les identifiants sont configurés en dur dans `infra/services/pgadmin/deployment.yaml` : `admin@example.com` / `pgadmin`. Modifiez-les dans ce manifeste avant le déploiement si nécessaire :

```bash
kubectl apply -f infra/services/pgadmin/
```

Dans pgAdmin, ajouter un serveur avec l’hôte `service-faces-postgres`, le port `5432`, la base `faces` et l’utilisateur `faces`.
