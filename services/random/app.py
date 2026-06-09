from fastapi import FastAPI, HTTPException, Depends, Header
from typing import Optional
import os
import random
import httpx
from auth import verify_token

app = FastAPI(title="Random service")

# URLs des services en amont (résolues via le DNS interne du cluster / docker-compose)
YEARS_SERVICE_URL = os.getenv("YEARS_SERVICE_URL", "http://service-years:8000")
ALBUMS_SERVICE_URL = os.getenv("ALBUMS_SERVICE_URL", "http://service-albums:8000")
HTTP_TIMEOUT = float(os.getenv("HTTP_TIMEOUT", "10"))

# Nombre d'images à sélectionner et nombre de tentatives pour trouver
# un album contenant suffisamment d'images.
IMAGE_COUNT = int(os.getenv("IMAGE_COUNT", "3"))
MAX_ATTEMPTS = int(os.getenv("MAX_ATTEMPTS", "10"))

IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic"}


def _get_json(client: httpx.Client, url: str, headers: dict):
    """Appelle un service en amont en propageant le token et les erreurs."""
    try:
        response = client.get(url, headers=headers)
    except httpx.RequestError as exc:
        raise HTTPException(status_code=502, detail=f"Upstream request failed: {exc}")

    if response.status_code == 401:
        # Le token n'a pas été accepté par le service en amont
        raise HTTPException(status_code=401, detail="Upstream authentication failed")
    if response.status_code >= 400:
        raise HTTPException(
            status_code=502,
            detail=f"Upstream error {response.status_code} for {url}",
        )

    return response.json()


def _collect_images(tree, prefix: str = "") -> list[str]:
    """Aplatit l'arbre renvoyé par le service albums et renvoie les chemins
    relatifs des fichiers image."""
    images: list[str] = []
    for entry in tree:
        name = entry.get("name", "")
        path = f"{prefix}/{name}" if prefix else name
        if entry.get("type") == "dir":
            images.extend(_collect_images(entry.get("children", []), path))
        else:
            ext = os.path.splitext(name)[1].lower()
            if ext in IMAGE_EXTENSIONS:
                images.append(path)
    return images


@app.get("/random")
def random_selection(
    authorization: Optional[str] = Header(None),
    claims: dict = Depends(verify_token),
):
    """Choisit aléatoirement une année, un album, puis quelques images de cet
    album, et renvoie l'ensemble en JSON.

    Le token OIDC fourni par l'appelant est vérifié localement puis retransmis
    aux services years et albums.
    """
    headers = {"Authorization": authorization}

    with httpx.Client(timeout=HTTP_TIMEOUT) as client:
        # 1. Liste de toutes les années disponibles
        years = _get_json(client, f"{YEARS_SERVICE_URL}/years", headers)
        if not years:
            raise HTTPException(status_code=404, detail="No years available")

        # On tente plusieurs fois afin d'éviter de tomber sur une année sans
        # album ou un album sans image.
        for _ in range(MAX_ATTEMPTS):
            year = random.choice(years)

            # 2. Liste des albums pour cette année
            albums = _get_json(client, f"{ALBUMS_SERVICE_URL}/albums/{year}", headers)
            if not albums:
                continue

            album = random.choice(albums)

            # 3. Contenu de l'album -> images
            tree = _get_json(
                client,
                f"{ALBUMS_SERVICE_URL}/albums/{year}/{album['id']}",
                headers,
            )
            images = _collect_images(tree)
            if not images:
                continue

            selected = random.sample(images, min(IMAGE_COUNT, len(images)))

            return {
                "year": year,
                "album": album,
                "images": selected,
            }

    raise HTTPException(
        status_code=404,
        detail="Could not find an album with images after several attempts",
    )
