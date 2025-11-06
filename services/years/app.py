from fastapi import FastAPI, HTTPException
from pathlib import Path
import os

app = FastAPI(title="Years service")

@app.get("/years")
def list_integer_folders():
    # Lire la variable d'environnement
    target_dir = os.getenv("TARGET_DIR", "/data")

    base_path = Path(target_dir)
    if not base_path.exists() or not base_path.is_dir():
        raise HTTPException(status_code=400, detail=f"Invalid directory: {target_dir}")

    # Lister les sous-dossiers dont le nom est un entier
    integer_folders = []
    for item in base_path.iterdir():
        if item.is_dir() and item.name.isdigit():
            integer_folders.append(int(item.name))

    integer_folders.sort()

    return integer_folders
