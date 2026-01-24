# Configuration de l'authentification JWT AWS Cognito

## Modifications apportées

### 1. Fichiers créés
- `services/years/auth.py` - Module de vérification JWT
- `services/albums/auth.py` - Module de vérification JWT
- `services/pictures/auth.py` - Module de vérification JWT

### 2. Fichiers modifiés

#### Requirements
Tous les fichiers `requirements.txt` ont été mis à jour pour inclure :
- `PyJWT[crypto]` - Pour la vérification des tokens JWT
- `cryptography` - Pour la gestion des clés cryptographiques

#### Services
Tous les services (`years`, `albums`, `pictures`) ont été modifiés pour :
- Importer la fonction `verify_token` depuis `auth.py`
- Ajouter `Depends(verify_token)` à toutes les routes protégées
- Retourner automatiquement 401 si le token est manquant, invalide ou expiré

### 3. Configuration requise

Les variables d'environnement suivantes doivent être configurées (valeurs par défaut) :
- `AWS_REGION=us-east-1`
- `USER_POOL_ID=us-east-1_bHxrl8GNt`

### 4. Fonctionnement

Le token JWT est vérifié automatiquement :
1. Extraction du token depuis le header `Authorization: Bearer <token>`
2. Récupération de la clé publique depuis AWS Cognito JWKS
3. Vérification de la signature, de l'expiration et de l'émetteur
4. Retour des claims du token si valide
5. Exception HTTP 401 si invalide

### 5. Rebuild des conteneurs

Après ces modifications, vous devez rebuilder les conteneurs Docker :

```bash
docker-compose build
docker-compose up -d
```

Ou si vous utilisez des images déjà construites :

```bash
docker build -t service-years ./services/years
docker build -t service-albums ./services/albums
docker build -t service-pictures ./services/pictures
```
