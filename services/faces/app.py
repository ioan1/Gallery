import os
import re
from urllib.parse import parse_qs, quote, urlparse

import numpy as np
import psycopg2
import requests
from fastapi import Depends, FastAPI, Header, HTTPException
from pydantic import BaseModel, Field

from auth import verify_token
from people import count_distinct_people

app = FastAPI(title="Faces service")

THUMBNAILS_BASE_URL = os.getenv("THUMBNAILS_BASE_URL", "https://gallery.redby.fr")
ALLOWED_HOST = os.getenv("ALLOWED_HOST", "gallery.redby.fr")
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://faces:faces@service-faces-postgres:5432/faces",
)
HTTP_TIMEOUT_SECONDS = float(os.getenv("HTTP_TIMEOUT_SECONDS", "20"))
MAX_IMAGE_BYTES = int(os.getenv("MAX_IMAGE_BYTES", str(50 * 1024 * 1024)))
FACE_DET_SIZE = int(os.getenv("FACE_DET_SIZE", "1024"))
FACE_DET_THRESHOLD = float(os.getenv("FACE_DET_THRESHOLD", "0.35"))
PERSON_SIMILARITY_THRESHOLD = float(os.getenv("PERSON_SIMILARITY_THRESHOLD", "0.45"))

SAFE_KEY_RE = re.compile(r"^[A-Za-z0-9._-]+$")


class FaceIndexRequest(BaseModel):
    category: str = Field(..., min_length=1, max_length=128)
    album: str = Field(..., min_length=1, max_length=128)
    name: str = Field(..., min_length=1, max_length=256)


def get_db_connection():
    return psycopg2.connect(DATABASE_URL)


def ensure_schema() -> None:
    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute("CREATE EXTENSION IF NOT EXISTS vector;")
            cur.execute(
                """
                CREATE TABLE IF NOT EXISTS images (
                    id SERIAL PRIMARY KEY,
                    category TEXT NOT NULL,
                    album TEXT NOT NULL,
                    image_name TEXT NOT NULL,
                    image_url TEXT NOT NULL,
                    created_at TIMESTAMPTZ DEFAULT NOW(),
                    UNIQUE (category, album, image_name)
                );
                """
            )
            cur.execute(
                """
                CREATE TABLE IF NOT EXISTS faces (
                    id SERIAL PRIMARY KEY,
                    image_id INT NOT NULL REFERENCES images(id) ON DELETE CASCADE,
                    face_type TEXT NOT NULL DEFAULT 'human',
                    embedding vector(512),
                    confidence FLOAT,
                    x INT,
                    y INT,
                    w INT,
                    h INT,
                    created_at TIMESTAMPTZ DEFAULT NOW()
                );
                """
            )
            cur.execute(
                "CREATE INDEX IF NOT EXISTS faces_embedding_hnsw_idx ON faces USING hnsw (embedding vector_cosine_ops);"
            )
            conn.commit()


ensure_schema()


def validate_image_key(category: str, album: str, name: str) -> str:
    if not category or not album or not name:
        raise ValueError("category, album and name are required")

    if not SAFE_KEY_RE.fullmatch(category):
        raise ValueError("Invalid category format")
    if not SAFE_KEY_RE.fullmatch(album):
        raise ValueError("Invalid album format")
    name_parts = name.split("/")
    if (
        len(name) > 256
        or "\\" in name
        or any(part in {"", ".", ".."} for part in name_parts)
        or any(ord(character) < 32 for character in name)
    ):
        raise ValueError("Invalid image name format")

    url = f"{THUMBNAILS_BASE_URL}/thumbnails/original/{category}/{album}?name={quote(name, safe='/')}"
    parsed = urlparse(url)

    if parsed.scheme != "https":
        raise ValueError("Only HTTPS urls are allowed")
    if parsed.hostname != ALLOWED_HOST:
        raise ValueError("Host not allowed")
    if not parsed.path.startswith("/thumbnails/original/"):
        raise ValueError("Invalid original image path")

    qs = parse_qs(parsed.query)
    if qs.get("name", [""])[0] != name:
        raise ValueError("Query parameter mismatch")

    return url


def download_image(image_url: str, authorization: str) -> bytes:
    response = requests.get(
        image_url,
        headers={"Authorization": authorization},
        timeout=HTTP_TIMEOUT_SECONDS,
        allow_redirects=False,
    )
    response.raise_for_status()

    content_type = response.headers.get("Content-Type", "")
    if "image/" not in content_type:
        raise ValueError("Target URL does not return an image")

    content = response.content
    if len(content) > MAX_IMAGE_BYTES:
        raise ValueError("Image is too large")

    return content


def detect_faces(image_bytes: bytes):
    try:
        from insightface.app import FaceAnalysis
    except ImportError as exc:  # pragma: no cover - depends on installed runtime libs
        raise RuntimeError("InsightFace is not installed") from exc

    try:
        import cv2
    except ImportError as exc:  # pragma: no cover
        raise RuntimeError("OpenCV is not installed") from exc

    try:
        app = FaceAnalysis(providers=["CPUExecutionProvider"])
        app.prepare(
            ctx_id=0,
            det_size=(FACE_DET_SIZE, FACE_DET_SIZE),
            det_thresh=FACE_DET_THRESHOLD,
        )
    except Exception as exc:  # pragma: no cover
        raise RuntimeError(f"Failed to initialize InsightFace: {exc}") from exc

    array = np.frombuffer(image_bytes, dtype=np.uint8)
    image = cv2.imdecode(array, cv2.IMREAD_COLOR)
    if image is None:
        raise ValueError("The downloaded content is not a valid image")

    faces = app.get(image)
    valid_faces = []

    for face in faces:
        if face.det_score < FACE_DET_THRESHOLD:
            continue

        bbox = face.bbox
        valid_faces.append(
            {
                "embedding": face.embedding.astype(float).tolist(),
                "confidence": float(face.det_score),
                "x": int(bbox[0]),
                "y": int(bbox[1]),
                "w": int(bbox[2] - bbox[0]),
                "h": int(bbox[3] - bbox[1]),
            }
        )

    return valid_faces


def save_face_records(category: str, album: str, name: str, image_url: str, faces: list[dict]) -> None:
    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(
                """
                INSERT INTO images (category, album, image_name, image_url)
                VALUES (%s, %s, %s, %s)
                ON CONFLICT (category, album, image_name)
                DO UPDATE SET image_url = EXCLUDED.image_url
                RETURNING id
                """,
                (category, album, name, image_url),
            )
            image_id = cur.fetchone()[0]
            cur.execute("DELETE FROM faces WHERE image_id = %s", (image_id,))
            cur.executemany(
                """
                INSERT INTO faces (image_id, face_type, embedding, confidence, x, y, w, h)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                """,
                [
                    (
                        image_id,
                        "human",
                        face["embedding"],
                        face["confidence"],
                        face["x"],
                        face["y"],
                        face["w"],
                        face["h"],
                    )
                    for face in faces
                ],
            )
        conn.commit()


@app.get("/health")
def health_check():
    return {"status": "ok", "service": "faces"}


@app.get("/faces/{year}/{album_id}/people-count")
def get_album_people_count(year: str, album_id: str, claims: dict = Depends(verify_token)):
    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(
                """
                SELECT faces.embedding::text
                FROM faces
                JOIN images ON images.id = faces.image_id
                WHERE images.category = %s AND images.album = %s
                  AND faces.embedding IS NOT NULL
                """,
                (year, album_id),
            )
            embeddings = [row[0] for row in cur.fetchall()]

    return {
        "year": year,
        "album": album_id,
        "people_count": count_distinct_people(embeddings, PERSON_SIMILARITY_THRESHOLD),
    }


@app.post("/faces/index")
def index_face(
    payload: FaceIndexRequest,
    authorization: str = Header(...),
    claims: dict = Depends(verify_token),
):
    try:
        image_url = validate_image_key(payload.category, payload.album, payload.name)
        image_bytes = download_image(image_url, authorization)
        faces = detect_faces(image_bytes)
        save_face_records(payload.category, payload.album, payload.name, image_url, faces)

        if not faces:
            return {
                "status": "no_face",
                "faces_detected": 0,
                "image_key": {
                    "category": payload.category,
                    "album": payload.album,
                    "name": payload.name,
                },
            }

        return {
            "status": "ok",
            "faces_detected": len(faces),
            "saved_faces": len(faces),
            "image_key": {
                "category": payload.category,
                "album": payload.album,
                "name": payload.name,
            },
            "image_url": image_url,
        }
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc)) from exc
    except Exception as exc:
        raise HTTPException(status_code=500, detail=str(exc)) from exc
