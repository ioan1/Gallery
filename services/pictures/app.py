from fastapi import FastAPI, HTTPException, Depends, Header
from pathlib import Path as PathLib
import os
import re
import hashlib
import json
from datetime import datetime
from fastapi.responses import JSONResponse, FileResponse
import httpx
from auth import verify_token

ALBUM_SERVICE_URL = os.getenv("ALBUM_SERVICE_URL", "http://service-albums:8000")

app = FastAPI(title="Pictures service")

def list_albums_for_year(year: int, authorization: str):
    url = f"{ALBUM_SERVICE_URL}/albums/{year}"

    try:
        with httpx.Client() as client:
            response = client.get(
                url, 
                timeout=5.0,
                headers={"Authorization": authorization}
            )
    except httpx.RequestError as e:
        raise HTTPException(
            status_code=502,
            detail=f"Erreur en appelant le service albums : {str(e)}"
        )
    
    if response.status_code != 200:
        raise HTTPException(
            status_code=response.status_code,
            detail=f"Service albums a répondu : {response.text}"
        )

    return response.json()

@app.get("/picture/{year}/{albumId}/{pictureName}")
def get_picture(
    year: int,
    albumId: str,
    pictureName: str,
    claims: dict = Depends(verify_token),
    authorization: str = Header(None)):
    albums = list_albums_for_year(year, authorization)
    album = next((a for a in albums if a["id"] == albumId), None)
    if not album:
        raise HTTPException(status_code=404, detail="Album not found")

    date_str = album["date"].replace("-", "")
    folder_name = f"{date_str} - {album['name']}"

    target_dir = os.getenv("TARGET_DIR", "/data")
    album_folder = PathLib(target_dir) / str(year) / folder_name

    if not album_folder.exists() or not album_folder.is_dir():
        raise HTTPException(status_code=404, detail="Album folder not found")

    picture_path = (album_folder / pictureName).resolve()
    if album_folder not in picture_path.parents and album_folder != picture_path.parent:
        raise HTTPException(status_code=400, detail="Invalid picture path")

    if not picture_path.exists() or not picture_path.is_file():
        raise HTTPException(status_code=404, detail="Picture not found")
    
    return FileResponse(path=picture_path.absolute())