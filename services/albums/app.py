from fastapi import FastAPI, HTTPException
from pathlib import Path
import os
import re
from datetime import datetime

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
                    albums.append({"date": formatted_date, "name": name})
                except ValueError:
                    # Skip invalid date formats
                    continue

    # Sort albums by date ascending
    albums.sort(key=lambda a: a["date"])

    return albums
