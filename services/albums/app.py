from fastapi import FastAPI, HTTPException
from pathlib import Path
import os
import re
import hashlib
from datetime import datetime
from fastapi.responses import JSONResponse

app = FastAPI(title="Albums service")


@app.get("/albums/{year}")
def list_albums_for_year(year: int):
    """
    List all albums for a given year.
    Each subfolder has the structure: 'YYYYMMDD - <name>'
    Returns a list of { "date": "YYYY-MM-DD", "name": "<name>" } objects, sorted by date asc.
    """
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

    return albums

@app.get("/albums/{year}/{albumId}")
def list_album_content(year: int, albumId: str):
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

def walk_dir(path: Path):
    # Ignore les dossiers/fichiers .DS_Store et @eaDir
    if path.name in [".DS_Store", "@eaDir"]:
        return None
    result = {"name": path.name, "type": "dir" if path.is_dir() else "file"}
    if path.is_dir():
        children = [
            walk_dir(child)
            for child in sorted(path.iterdir())
            if child.name not in [".DS_Store", "@eaDir"]
        ]
        result["children"] = [c for c in children if c is not None]
    return result

    return JSONResponse(walk_dir(album_folder))
