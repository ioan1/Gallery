from fastapi import FastAPI, HTTPException
from pathlib import Path
import os
import re
import hashlib
import redis
import json
from datetime import datetime
from fastapi.responses import JSONResponse

app = FastAPI(title="Albums service")

# Connexion Redis
REDIS_HOST = os.getenv("REDIS_HOST", "service-cache")
REDIS_PORT = int(os.getenv("REDIS_PORT", "6379"))
REDIS_TTL = int(os.getenv("REDIS_TTL", "300"))
CACHE_KEY_ALBUMS = "albums"
CACHE_KEY_FILES = "album_files"

redis_client = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=0, decode_responses=True)

@app.get("/albums/{year}")
def list_albums_for_year(year: int):
    cache_key = f"{CACHE_KEY_ALBUMS}:{year}"
    cached = redis_client.get(cache_key)
    if cached:
        return json.loads(cached)
    
    # Read the target directory from environment variable
    target_dir = os.getenv("TARGET_DIR", "/data")

    base_path = Path(target_dir)
    if not base_path.exists() or not base_path.is_dir():
        raise HTTPException(status_code=400, detail=f"Invalid directory: {target_dir}")

    # Construct the path for the given year
    year_path = base_path / str(year)
    if not year_path.exists() or not year_path.is_dir():
        raise HTTPException(status_code=404, detail=f"No albums found for year {year}")

    # Regex pattern for folder names like "YYYYMMDD - Something"
    pattern = re.compile(r"^(\d{8})\s*-\s*(.+)$")

    albums = []
    for item in year_path.iterdir():
        if item.is_dir():
            match = pattern.match(item.name)
            if match:
                date_str, name = match.groups()
                try:
                    # Convert YYYYMMDD to YYYY-MM-DD
                    formatted_date = datetime.strptime(date_str, "%Y%m%d").date().isoformat()
                    id = hashlib.blake2b(item.name.encode(), digest_size=4).hexdigest()
                    albums.append({"date": formatted_date, "name": name, "id": id})
                except ValueError:
                    # Skip invalid date formats
                    continue

    # Sort albums by date ascending
    albums.sort(key=lambda a: a["date"])

    redis_client.set(cache_key, json.dumps(albums), ex=REDIS_TTL)

    return albums

@app.get("/albums/{year}/{albumId}")
def list_album_content(year: int, albumId: str):
    cache_key = f"{CACHE_KEY_FILES}:{year}:{albumId}"
    cached = redis_client.get(cache_key)
    if cached:
        return JSONResponse(content=json.loads(cached))

    albums = list_albums_for_year(year)
    album = next((a for a in albums if a["id"] == albumId), None)
    if not album:
        raise HTTPException(status_code=404, detail="Album not found")

    date_str = album["date"].replace("-", "")
    folder_name = f"{date_str} - {album['name']}"

    target_dir = os.getenv("TARGET_DIR", "/data")
    album_folder = Path(target_dir) / str(year) / folder_name

    if not album_folder.exists() or not album_folder.is_dir():
        raise HTTPException(status_code=404, detail="Album folder not found")

    tree = walk_dir(album_folder)
    redis_client.set(cache_key, json.dumps(tree), ex=REDIS_TTL)
    return JSONResponse(content=tree)

def walk_dir(path: Path):
    # Retourne une liste d'objets représentant les fichiers et dossiers du dossier courant
    entries = []
    for child in sorted(path.iterdir()):
        if child.name in [".DS_Store", "@eaDir"]:
            continue
        entry = {"name": child.name, "type": "dir" if child.is_dir() else "file"}
        if child.is_dir():
            entry["children"] = [
                c for c in walk_dir(child)
            ]
        entries.append(entry)
    return entries

