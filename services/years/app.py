from fastapi import FastAPI, HTTPException
from pathlib import Path
import os
import redis
import json

app = FastAPI(title="Years service")

# Connexion Redis
REDIS_HOST = os.getenv("REDIS_HOST", "service-cache")
REDIS_PORT = int(os.getenv("REDIS_PORT", "6379"))
REDIS_TTL = int(os.getenv("REDIS_TTL", "300"))
CACHE_KEY = "years"

redis_client = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=0, decode_responses=True)

@app.get("/years")
def list_integer_folders():
    
    cached = redis_client.get(CACHE_KEY)
    if cached:
        return json.loads(cached)

    target_dir = os.getenv("TARGET_DIR", "/data")
    base_path = Path(target_dir)
    if not base_path.exists() or not base_path.is_dir():
        raise HTTPException(status_code=400, detail=f"Invalid directory: {target_dir}")

    integer_folders = []
    for item in base_path.iterdir():
        if item.is_dir() and item.name.isdigit():
            integer_folders.append(int(item.name))
    integer_folders.sort()

    redis_client.set(CACHE_KEY, json.dumps(integer_folders), ex=REDIS_TTL)

    return integer_folders